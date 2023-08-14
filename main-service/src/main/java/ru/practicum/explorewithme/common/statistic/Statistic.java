package ru.practicum.explorewithme.common.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.statistic.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.constants.TimePattern.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class Statistic {
    private final StatisticClient statisticClient;
    private final EventRepository eventRepository;

    public void postStat(List<Event> events, HttpServletRequest httpServletRequest) {
        if (events != null && !events.isEmpty()) {
            for (Event event : events) {
                statisticClient.postStat("/events/" + event.getId(), httpServletRequest, "main-service");
            }
        }
    }

    public void postStat(HttpServletRequest httpServletRequest) {
        statisticClient.postStat(httpServletRequest, "main-service");
    }

    public void getEventStat(Event event) {
        String startTime = event.getCreatedOn().format(DATE_TIME_FORMATTER);
        String endTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        List<String> uris = List.of("/events/" + event.getId());
        List<ViewStatsDto> viewStatsDtos = statisticClient.getStat(startTime, endTime, uris, true);
        if (!viewStatsDtos.isEmpty()) {
            event.setViews(viewStatsDtos.get(0).getHits());
        } else {
            event.setViews(0L);
        }
        eventRepository.save(event);
    }

    public void getEventsStat(List<Event> events) {
        if (events != null && !events.isEmpty()) {
            for (Event event : events) {
                getEventStat(event);
            }
        }
    }


}
