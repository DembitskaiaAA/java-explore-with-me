package ru.practicum.explorewithme.privy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.eventDto.EventFullDto;
import ru.practicum.explorewithme.dto.eventDto.EventShortDto;
import ru.practicum.explorewithme.dto.eventDto.NewEventDto;
import ru.practicum.explorewithme.dto.eventDto.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.requestDto.ParticipationRequestDto;
import ru.practicum.explorewithme.service.eventService.EventService;
import ru.practicum.explorewithme.service.requestService.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
public class PrivyControllerEvents {
    @Autowired
    private EventService eventService;
    @Autowired
    private RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserId(@PathVariable Integer userId,
                                                 @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{}/events PARAMS from {}, size {}", userId, from, size);
        return eventService.getEventsByUserId(userId, Pagination.splitByPages(from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Integer userId,
                                  @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events BODY NewEventDto {}", userId, newEventDto);
        return eventService.postEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdAndUserId(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("GET /users/{}/events/{}", userId, eventId);
        return eventService.getEventByIdAndUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH /users/{}/events/{} BODY UpdateEventUserRequest {}", userId, eventId, updateEventUserRequest);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getEventParticipants(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId) {
        log.info("GET /users/{}/events/{}/requests ", userId, eventId);
        return eventService.getEventParticipants(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH /users/{}/events/{}/requests BODY  EventRequestStatusUpdateRequest {}", userId, eventId, eventRequestStatusUpdateRequest);
        return requestService.changeRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }


}
