package ru.practicum.explorewithme.service.commentService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.commentDto.CommentDto;
import ru.practicum.explorewithme.dto.commentDto.NewCommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface CommentService {
    CommentDto postComment(Integer eventId, Integer userId, NewCommentDto newCommentDto);

    void deleteComment(Integer commentId, Integer userId);

    List<CommentDto> getUserComments(Integer userId, Pageable pageable);

    List<CommentDto> getEventComments(Integer eventId, Integer userId, Pageable pageable);

    CommentDto updateComment(Integer commentId, Integer userId, NewCommentDto newCommentDto);

    void deleteCommentByAdmin(Integer commentId);

    List<CommentDto> getUserCommentsByAdmin(Integer userId, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<CommentDto> getEventCommentsByAdmin(Integer eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
