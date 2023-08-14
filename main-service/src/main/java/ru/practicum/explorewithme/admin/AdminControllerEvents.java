package ru.practicum.explorewithme.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.enums.PublishingStatus;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.eventDto.EventFullDto;
import ru.practicum.explorewithme.dto.eventDto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.service.eventService.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/events")
public class AdminControllerEvents {

    @Autowired
    private EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Integer> users,
                                               @RequestParam(required = false) List<PublishingStatus> states,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeEnd,
                                               @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /admin/events PARAMS users {}, states {}, categories {}, rangeStart {}, rangeEnd {}, from {}, size {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, Pagination.splitByPages(from, size));
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto changeEventAndStatus(@PathVariable Integer eventId,
                                             @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH /admin/events/{}  BODY UpdateEventAdminRequest {}", eventId, updateEventAdminRequest);
        return eventService.changeEventAndStatus(eventId, updateEventAdminRequest);
    }

}
