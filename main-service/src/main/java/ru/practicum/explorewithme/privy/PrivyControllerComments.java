package ru.practicum.explorewithme.privy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.commentDto.CommentDto;
import ru.practicum.explorewithme.dto.commentDto.NewCommentDto;
import ru.practicum.explorewithme.service.commentService.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users/{userId}/comments")
public class PrivyControllerComments {

    @Autowired
    private CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Integer userId,
                                  @PathVariable Integer eventId,
                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST /users/{}/comments/events/{} BODY NewCommentDto {}", userId, eventId, newCommentDto);
        return commentService.postComment(eventId, userId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        log.info("DELETE /users/{}/comments/{}", userId, commentId);
        commentService.deleteComment(commentId, userId);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Integer userId,
                                    @PathVariable Integer commentId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("PATCH /users/{}/comments/{} BODY NewCommentDto {}", userId, commentId, newCommentDto);
        return commentService.updateComment(commentId, userId, newCommentDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUserComments(@PathVariable Integer userId,
                                            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{}/comments PARAMS from {}, size {}", userId, from, size);
        return commentService.getUserComments(userId, Pagination.splitByPages(from, size));
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(@PathVariable Integer userId,
                                             @PathVariable Integer eventId,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{}/comments/events/{} from {}, size {}", userId, eventId, from, size);
        return commentService.getEventComments(eventId, userId, Pagination.splitByPages(from, size));
    }

}
