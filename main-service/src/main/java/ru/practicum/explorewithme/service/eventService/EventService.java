package ru.practicum.explorewithme.service.eventService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.enums.PublishingStatus;
import ru.practicum.explorewithme.common.enums.TypesSortEvents;
import ru.practicum.explorewithme.dto.eventDto.*;
import ru.practicum.explorewithme.dto.requestDto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface EventService {
    EventFullDto postEvent(Integer userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUserId(Integer userId, Pageable pageable);

    EventFullDto getEventByIdAndUserId(Integer userId, Integer eventId);

    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventShortDto> getEventsByUsers(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, TypesSortEvents sort, Pageable pageable, HttpServletRequest httpServletRequest);

    EventFullDto getEventById(Integer id, HttpServletRequest httpServletRequest);

    List<ParticipationRequestDto> getEventParticipants(Integer userId, Integer eventId);

    List<EventFullDto> getEventsByAdmin(List<Integer> users, List<PublishingStatus> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto changeEventAndStatus(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
