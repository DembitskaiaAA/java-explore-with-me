package ru.practicum.explorewithme.service.requestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.checks.CheckExist;
import ru.practicum.explorewithme.common.enums.RequestStatus;
import ru.practicum.explorewithme.common.enums.TypesRequestsResponses;
import ru.practicum.explorewithme.dto.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.requestDto.ParticipationRequestDto;
import ru.practicum.explorewithme.exceptions.ConditionException;
import ru.practicum.explorewithme.mapper.requestMapper.RequestMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Request;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestServiceImp implements RequestService {
    @Autowired
    private CheckExist checkExist;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RequestMapper requestMapper;

    @Override
    public ParticipationRequestDto postRequest(Integer userId, Integer eventId) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when posting request: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when posting request: event with id: %s is missing", eventId));
        if (requestRepository.findFirstByEvent_IdAndRequester_Id(eventId, userId) != null) {
            throw new ConditionException("Error when sending request: unable to resubmit event request");
        }
        if (event.getInitiator() == user) {
            throw new ConditionException("Error when sending request: event initiator cannot make a request for its event");
        }
        if (event.getPublishedOn() == null) {
            throw new ConditionException("Error when posting request: event must be published");
        }
        if (event.getParticipantLimit() != 0 && Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new ConditionException("Error when sending request: event has reached participation request limit");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        Request savedRequest = requestRepository.save(request);
        setConfirmedRequestsForEvent(event);
        return requestMapper.transformRequestToParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUserId(Integer userId) {
        checkExist.checkUserOnExist(userId, String.format("Error when getting requests: user with id: %s is missing", userId));
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        return requests.stream().map(x -> requestMapper.transformRequestToParticipationRequestDto(x)).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        checkExist.checkUserOnExist(userId, String.format("Error when canceling request: user with id: %s is missing", userId));
        Request request = checkExist.checkRequestOnExist(requestId, String.format("Error when canceling request: request with id: %s is missing", requestId));
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new ConditionException("Error when canceling request: only the creator of the request can cancel it");
        }
        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);
        setConfirmedRequestsForEvent(savedRequest.getEvent());
        return requestMapper.transformRequestToParticipationRequestDto(savedRequest);
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        checkExist.checkUserOnExist(userId, String.format("Error when updating event: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when updating: event with id: %s is missing", eventId));
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        Set<ParticipationRequestDto> confirmedRequests = new HashSet<>();
        Set<ParticipationRequestDto> rejectedRequests = new HashSet<>();
        if (!event.getRequestModeration()) {
            return result;
        }
        if (event.getParticipantLimit() == 0) {
            for (Integer i : eventRequestStatusUpdateRequest.getRequestIds()) {
                Request request = checkExist.checkRequestOnExist(i, String.format("Error when changing request status: request with id: %s is missing", i));
                request.setStatus(RequestStatus.CONFIRMED);
                Request savedRequest = requestRepository.save(request);
                setConfirmedRequestsForEvent(savedRequest.getEvent());
            }
            return result;
        }
        if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new ConditionException("Error when changing request status: event has reached participation request limit");
        }
        for (Integer i : eventRequestStatusUpdateRequest.getRequestIds()) {
            Request request = checkExist.checkRequestOnExist(i, String.format("Error when changing request status: request with id: %s is missing", i));
            if (eventRequestStatusUpdateRequest.getStatus().equals(TypesRequestsResponses.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                Request savedRequest = requestRepository.save(request);
                rejectedRequests.add(requestMapper.transformRequestToParticipationRequestDto(savedRequest));
            } else {
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    if (request.getStatus().equals(RequestStatus.PENDING)) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        Request savedRequest = requestRepository.save(request);
                        setConfirmedRequestsForEvent(savedRequest.getEvent());
                        confirmedRequests.add(requestMapper.transformRequestToParticipationRequestDto(savedRequest));
                    } else {
                        throw new ConditionException("Error when changing request status: request must have status PENDING");
                    }
                } else {
                    List<Request> requests = requestRepository.findAllByEvent_IdAndStatusIs(eventId, RequestStatus.PENDING);
                    rejectedRequests = requests.stream().peek(x -> x.setStatus(RequestStatus.REJECTED)).map(x -> requestMapper.transformRequestToParticipationRequestDto(x)).collect(Collectors.toSet());
                    break;
                }
            }
        }
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

    public void setConfirmedRequestsForEvent(Event event) {
        List<Request> requests = requestRepository.findAllByEvent_IdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);
        if (!requests.isEmpty()) {
            event.setConfirmedRequests(requests.size());
        } else {
            event.setConfirmedRequests(0);
        }
        eventRepository.save(event);
    }

}
