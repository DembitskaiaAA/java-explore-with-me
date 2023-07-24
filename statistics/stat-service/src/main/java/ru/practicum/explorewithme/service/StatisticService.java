package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStats;

import java.sql.Timestamp;
import java.util.List;

public interface StatisticService {
    void postStatistic(EndpointHitDto endpointHitDto);

    List<ViewStats> getStatistic(Timestamp start, Timestamp end, List<String> uris, Boolean unique);
}
