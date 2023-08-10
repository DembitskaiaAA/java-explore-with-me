package ru.practicum.explorewithme.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.enums.TypesSortEvents;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.eventDto.EventFullDto;
import ru.practicum.explorewithme.dto.eventDto.EventShortDto;
import ru.practicum.explorewithme.service.eventService.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
public class SharedControllerEvents {

    @Autowired
    private EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUsers(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Integer> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeStart,
                                                @RequestParam(required = false) @Future @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime rangeEnd,
                                                @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(required = false) TypesSortEvents sort,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                                HttpServletRequest httpServletRequest) {
        log.info("GET /events PARAMS text {}, categories {}, paid {}, rangeStart {}, rangeEnd {}, onlyAvailable {}" +
                ", sort {}, from {}, size {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEventsByUsers(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, Pagination.splitByPages(from, size), httpServletRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Integer id,
                                     HttpServletRequest httpServletRequest) {
        log.info("GET /events/{}", id);
        return eventService.getEventById(id, httpServletRequest);
    }
}
