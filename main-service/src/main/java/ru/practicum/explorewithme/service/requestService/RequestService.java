package ru.practicum.explorewithme.service.requestService;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.requestDto.ParticipationRequestDto;

import java.util.List;

@Service
public interface RequestService {
    ParticipationRequestDto postRequest(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getRequestsByUserId(Integer userId);

    ParticipationRequestDto cancelRequest(Integer userId, Integer requestId);

    EventRequestStatusUpdateResult changeRequestStatus(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
