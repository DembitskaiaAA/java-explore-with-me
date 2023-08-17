package ru.practicum.explorewithme.service.eventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.checks.CheckExist;
import ru.practicum.explorewithme.common.enums.PublishingStatus;
import ru.practicum.explorewithme.common.enums.StateAction;
import ru.practicum.explorewithme.common.enums.TypesEventsAdminResponses;
import ru.practicum.explorewithme.common.enums.TypesSortEvents;
import ru.practicum.explorewithme.common.statistic.Statistic;
import ru.practicum.explorewithme.dto.eventDto.*;
import ru.practicum.explorewithme.dto.requestDto.ParticipationRequestDto;
import ru.practicum.explorewithme.exceptions.ConditionException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mapper.eventMapper.EventMapper;
import ru.practicum.explorewithme.mapper.requestMapper.RequestMapper;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.LocationRepository;
import ru.practicum.explorewithme.repository.RequestRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class EventServiceImp implements EventService {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CheckExist checkExist;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestMapper requestMapper;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Statistic statistic;
    @Autowired
    private LocationRepository locationRepository;


    @Override
    public EventFullDto postEvent(Integer userId, NewEventDto newEventDto) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when posting event: user with id: %s is missing", userId));
        Category category = checkExist.checkCategoryOnExist(newEventDto.getCategory(), String.format("Error when posting event: category with id: %s is missing", newEventDto.getCategory()));
        Event event = eventMapper.transformNewEventDtoToEvent(newEventDto);
        Location location = locationRepository.save(newEventDto.getLocation());
        event.setLocation(location);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setState(PublishingStatus.PENDING);
        return eventMapper.transformEventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Integer userId, Pageable pageable) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when getting events: user with id: %s is missing", userId));
        List<Event> events = eventRepository.findAllByInitiatorOrderByEventDateDesc(user, pageable);
        return events.stream().map(x -> eventMapper.transformEventToEventShortDto(x)).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByIdAndUserId(Integer userId, Integer eventId) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when getting event: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when getting event: event with id: %s is missing", eventId));
        if (event.getInitiator() != user) {
            throw new NotFoundException(String.format("Error when getting event: the event was not found, the user id: %s of the organizer of the event or the event id^ %s may be incorrect", user, eventId));
        }
        return eventMapper.transformEventToEventFullDto(event);
    }


    @Override
    public EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when updating event: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when updating: event with id: %s is missing", eventId));
        if (updateEventUserRequest == null) {
            return eventMapper.transformEventToEventFullDto(event);
        }

        if (event.getInitiator() != user) {
            throw new NotFoundException(String.format("Error when updating: the event was not found, the user id: %s of the organizer of the event or the event id^ %s may be incorrect", user, eventId));
        }
        if (event.getState().equals(PublishingStatus.PUBLISHED)) {
            throw new ConditionException("Error when updating: only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            Category category = checkExist.checkCategoryOnExist(updateEventUserRequest.getCategory(), String.format("Error when updating event: category with id: %s is missing", updateEventUserRequest.getCategory()));
            event.setCategory(category);
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(PublishingStatus.PENDING);
            } else event.setState(PublishingStatus.CANCELED);
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        return eventMapper.transformEventToEventFullDto(eventRepository.save(event));
    }


    @Override
    public List<EventShortDto> getEventsByUsers(String text, List<Integer> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Boolean onlyAvailable, TypesSortEvents sort, Pageable pageable,
                                                HttpServletRequest httpServletRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("state"), PublishingStatus.PUBLISHED));
        if (text != null) {
            Expression<String> textPredicate = builder.lower(builder.literal("%" + text + "%"));
            Predicate annotationPredicate = builder.like(builder.lower(root.get("annotation")), textPredicate);
            Predicate descriptionPredicate = builder.like(builder.lower(root.get("description")), textPredicate);
            predicates.add(builder.or(annotationPredicate, descriptionPredicate));
        }
        if (categories != null && !categories.isEmpty()) {
            Join<Event, Category> categoryJoin = root.join("category");
            predicates.add(categoryJoin.get("id").in(categories));
        }
        if (paid != null) {
            predicates.add(builder.equal(root.get("paid"), paid));
        }
        if (rangeStart != null && rangeEnd != null) {
            predicates.add(builder.between(root.get("eventDate"), rangeStart, rangeEnd));
        } else if (rangeStart != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        } else if (rangeEnd != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        if (onlyAvailable) {
            predicates.add(builder.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }
        Predicate finalPredicate = builder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(finalPredicate);
        criteriaQuery.orderBy(builder.asc(root.get("eventDate")));

        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Event> result = typedQuery.getResultList();
        if (result.size() == 0) {
            return new ArrayList<>();
        }
        if (sort != null) {
            if (sort.equals(TypesSortEvents.EVENT_DATE)) {
                result = result.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
            } else {
                result = result.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
            }
        }
        statistic.postStat(result, httpServletRequest);
        statistic.getEventsStat(result);


        return result.stream()
                .map(x -> eventMapper.transformEventToEventShortDto(x))
                .collect(Collectors.toList());
    }


    @Override
    public EventFullDto getEventById(Integer id, HttpServletRequest httpServletRequest) {
        Event event = checkExist.checkEventOnExist(id, String.format("Error when getting event: event with id: %s is missing", id));
        if (event.getPublishedOn() == null) {
            throw new NotFoundException("Error when getting event: event must be published");
        }
        statistic.postStat(httpServletRequest);
        statistic.getEventStat(event);
        return eventMapper.transformEventToEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Integer userId, Integer eventId) {
        User user = checkExist.checkUserOnExist(userId, String.format("Error when updating event: user with id: %s is missing", userId));
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when updating: event with id: %s is missing", eventId));
        if (event.getInitiator() != user) {
            throw new ConditionException("Error when getting event's participants: the list of participants can be obtained by the organizer of the event");
        }
        List<Request> requests = requestRepository.findAllByEvent_Id(eventId);
        return requests.stream().map(x -> requestMapper.transformRequestToParticipationRequestDto(x)).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Integer> users,
                                               List<PublishingStatus> states,
                                               List<Integer> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (users != null) {
            Join<Event, User> initiatorJoin = root.join("initiator");
            predicates.add(initiatorJoin.get("id").in(users));
        }
        if (states != null) {
            predicates.add(builder.in(root.get("state")).value(states));
        }
        if (categories != null) {
            Join<Event, Category> categoryJoin = root.join("category");
            predicates.add(categoryJoin.get("id").in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            predicates.add(builder.between(root.get("eventDate"), rangeStart, rangeEnd));
        } else if (rangeStart != null) {
            predicates.add(builder.greaterThan(root.get("eventDate"), rangeStart));
        } else if (rangeEnd != null) {
            predicates.add(builder.lessThan(root.get("eventDate"), rangeEnd));
        }
        Predicate finalPredicate = builder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(finalPredicate);
        criteriaQuery.orderBy(builder.desc(root.get("eventDate")));
        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Event> result = typedQuery.getResultList();
        if (result.size() == 0) {
            return new ArrayList<>();
        }

        return result.stream()
                .map(x -> eventMapper.transformEventToEventFullDto(x))
                .collect(Collectors.toList());
    }


    @Override
    public EventFullDto changeEventAndStatus(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkExist.checkEventOnExist(eventId, String.format("Error when updating: event with id: %s is missing", eventId));
        if (updateEventAdminRequest == null) {
            return eventMapper.transformEventToEventFullDto(event);
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = checkExist.checkCategoryOnExist(updateEventAdminRequest.getCategory(), String.format("Error when updating event: category with id: %s is missing", updateEventAdminRequest.getCategory()));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = checkExist.checkLocationOnExist(eventId, String.format("Error when updating event: location with id: %s is missing", updateEventAdminRequest.getLocation()));
            event.setLocation(location);
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(TypesEventsAdminResponses.PUBLISH_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new ConditionException("Event already published");
                }
                if (event.getState().equals(PublishingStatus.CANCELED)) {
                    throw new ConditionException("Event already canceled");
                }
                event.setState(PublishingStatus.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                event.setComments(new HashSet<>());
                event.setViews(0L);
            } else if (updateEventAdminRequest.getStateAction().equals(TypesEventsAdminResponses.REJECT_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new ConditionException("Event already published");
                }
                event.setState(PublishingStatus.CANCELED);
            }
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventAdminRequest.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now())) {
                throw new ConditionException("The start date of the event to be modified must be greater than now");
            }
            if (event.getPublishedOn() != null) {
                if (eventDateTime.isBefore(event.getPublishedOn().plusHours(1))) {
                    throw new ConditionException("The start date of the event to be modified must be less than one hour from the publication date");
                }
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        return eventMapper.transformEventToEventFullDto(eventRepository.save(event));
    }

}
