package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
    void postStatistic(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
