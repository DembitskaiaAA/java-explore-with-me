package ru.practicum.explorewithme.service.commentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.checks.CheckExist;
import ru.practicum.explorewithme.common.enums.PublishingStatus;
import ru.practicum.explorewithme.dto.commentDto.CommentDto;
import ru.practicum.explorewithme.dto.commentDto.NewCommentDto;
import ru.practicum.explorewithme.exceptions.ConditionException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.exceptions.RequestParametersException;
import ru.practicum.explorewithme.mapper.commentMapper.CommentMapper;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImp implements CommentService {
    @Autowired
    private CheckExist checkExist;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommentMapper commentMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CommentDto postComment(Integer eventId, Integer userId, NewCommentDto newCommentDto) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when creating comment: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when creating comment: event with id: %s is missing", eventId));
        if (!event.getState().equals(PublishingStatus.PUBLISHED)) {
            throw new ConditionException(String.format("The comment can only be left on a published event. Event %s isn't published", eventId));
        }
        Comment savedComment = commentRepository.save(
                Comment.builder()
                        .commentator(user)
                        .text(newCommentDto.getText())
                        .created(LocalDateTime.now())
                        .event(event)
                        .build()
        );

        Set<Comment> savedComments = event.getComments();
        savedComments.add(savedComment);
        event.setComments(savedComments);
        eventRepository.save(event);

        return commentMapper.transformCommentToCommentDto(savedComment);
    }

    @Override
    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = checkExist.checkCommentOnExist(commentId, String.format("Error when deleting comment: comment with id: %s is missing", commentId));
        User user = checkExist.checkUserOnExist(userId, String.format("Error when deleting comment: user with id: %s is missing", userId));
        Event event = comment.getEvent();
        if (!event.getState().equals(PublishingStatus.PUBLISHED)) {
            throw new ConditionException(String.format("The comment can be delete on a published event. Event %s isn't published", event.getId()));
        }
        if (comment.getCommentator() == user) {
            commentRepository.delete(comment);
        } else {
            throw new ConditionException(String.format("Only the user or administrator who created it can delete a comment. The user %s is not the creator of the comment.", userId));
        }
    }

    @Override
    public List<CommentDto> getUserComments(Integer userId, Pageable pageable) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when getting comments by userId: user with id: %s is missing", userId));
        List<Comment> comments = commentRepository.findAllByCommentator_IdOrderByCreatedDesc(userId, pageable);
        return comments.stream().filter(x -> x.getCommentator() == user).map(x -> commentMapper.transformCommentToCommentDto(x)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getEventComments(Integer eventId, Integer userId, Pageable pageable) {
        checkExist.checkUserOnExist(userId, String.format("Error when getting comments by eventId: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when getting comments by eventId: event with id: %s is missing", eventId));
        if (!event.getState().equals(PublishingStatus.PUBLISHED)) {
            throw new ConditionException(String.format("You can only get comments on a published event. Event %s isn't published", eventId));
        }

        List<CommentDto> commentList = event.getComments().stream()
                .map(x -> commentMapper.transformCommentToCommentDto(x))
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<CommentDto> pageContent;

        if (commentList.size() < startItem) {
            pageContent = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, commentList.size());
            pageContent = commentList.subList(startItem, toIndex);
        }

        return pageContent;
    }

    @Override
    public CommentDto updateComment(Integer commentId, Integer userId, NewCommentDto newCommentDto) {
        Comment comment = checkExist.checkCommentOnExist(commentId, String.format("Error when updating comment: comment with id: %s is missing", commentId));
        User user = checkExist.checkUserOnExist(userId, String.format("Error when updating: user with id: %s is missing", userId));
        if (!comment.getEvent().getState().equals(PublishingStatus.PUBLISHED)) {
            throw new ConditionException(String.format("You can only update comment on a published event. Event %s isn't published", comment.getEvent().getId()));
        }
        if (comment.getCommentator() != user) {
            throw new NotFoundException(String.format("The user %s did not create a comment %s", userId, commentId));
        }
        comment.setText(newCommentDto.getText());
        return commentMapper.transformCommentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByAdmin(Integer commentId) {
        Comment comment = checkExist.checkCommentOnExist(commentId, String.format("Error when deleting comment by admin: comment with id: %s is missing", commentId));
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> getUserCommentsByAdmin(Integer userId, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when getting user's comments by admin : user with id: %s is missing", userId));
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("commentator"), user));
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new RequestParametersException(String.format("Error when searching for comments: invalid time period start: %s, end: %s", rangeStart, rangeEnd));
            }
            predicates.add(criteriaBuilder.between(root.get("created"), rangeStart, rangeEnd));
        } else if (rangeStart != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), rangeStart));
        } else if (rangeEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), rangeEnd));
        }
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(finalPredicate);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("created")));

        TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Comment> result = typedQuery.getResultList();
        return result.stream().map(x -> commentMapper.transformCommentToCommentDto(x)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getEventCommentsByAdmin(Integer eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when getting event's comments by admin: event with id: %s is missing", eventId));
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("event"), event));
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new RequestParametersException(String.format("Error when searching for comments: invalid time period start: %s, end: %s", rangeStart, rangeEnd));
            }
            predicates.add(criteriaBuilder.between(root.get("created"), rangeStart, rangeEnd));
        } else if (rangeStart != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), rangeStart));
        } else if (rangeEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), rangeEnd));
        }
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(finalPredicate);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("created")));

        TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Comment> result = typedQuery.getResultList();
        return result.stream().map(x -> commentMapper.transformCommentToCommentDto(x)).collect(Collectors.toList());
    }
}
