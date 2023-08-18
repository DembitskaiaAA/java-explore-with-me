package ru.practicum.explorewithme.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.commentDto.CommentDto;
import ru.practicum.explorewithme.service.commentService.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/comments")
public class AdminControllerComments {
    @Autowired
    private CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Integer commentId) {
        log.info("DELETE /admin/comments/{}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUserCommentsByAdmin(@PathVariable Integer userId,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeStart,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeEnd,
                                                   @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /admin/comments/users/{} PARAMS rangeStart {}, rangeEnd {}, from {}, size {}", userId, rangeStart, rangeEnd, from, size);
        return commentService.getUserCommentsByAdmin(userId, rangeStart, rangeEnd, Pagination.splitByPages(from, size));
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventCommentsByAdmin(@PathVariable Integer eventId,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeStart,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeEnd,
                                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /admin/comments/events/{} PARAMS rangeStart {}, rangeEnd {}, from {}, size {}", eventId, rangeStart, rangeEnd, from, size);
        return commentService.getEventCommentsByAdmin(eventId, rangeStart, rangeEnd, Pagination.splitByPages(from, size));
    }
}
