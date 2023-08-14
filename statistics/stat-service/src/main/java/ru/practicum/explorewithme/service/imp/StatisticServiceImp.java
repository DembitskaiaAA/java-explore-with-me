package ru.practicum.explorewithme.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.exceptions.EndTimeInThePastException;
import ru.practicum.explorewithme.mapper.StatMapper;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.StatisticRepository;
import ru.practicum.explorewithme.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImp implements StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private StatMapper statMapper;

    @Override
    public void postStatistic(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statMapper.transformEndpointHitDtoToEndpointHit(endpointHitDto);
        statisticRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (end.isBefore(start)) {
            throw new EndTimeInThePastException("Invalid end time");
        }
        if (unique) {
            if (uris == null || uris.size() == 0) {
                List<ViewStats> viewStats = statisticRepository.findAllAfterStartAndBeforeEndWithUnique(start, end);

                return viewStats.stream().map(statMapper::transformViewStatsToViewStatsDto).collect(Collectors.toList());
            } else {
                List<ViewStats> viewStats = statisticRepository.findAllAfterStartAndBeforeEndByUrisWithUnique(start, end, uris);
                return viewStats.stream().map(statMapper::transformViewStatsToViewStatsDto).collect(Collectors.toList());
            }
        } else {
            if (uris == null || uris.size() == 0) {
                List<ViewStats> viewStats = statisticRepository.findAllAfterStartAndBeforeEnd(start, end);
                return viewStats.stream().map(statMapper::transformViewStatsToViewStatsDto).collect(Collectors.toList());
            } else {
                List<ViewStats> viewStats = statisticRepository.findAllAfterStartAndBeforeEndByUris(start, end, uris);
                return viewStats.stream().map(statMapper::transformViewStatsToViewStatsDto).collect(Collectors.toList());
            }
        }
    }
}
