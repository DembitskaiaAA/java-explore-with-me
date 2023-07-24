package ru.practicum.explorewithme.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.mapper.StatMapper;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.StatisticRepository;
import ru.practicum.explorewithme.service.StatisticService;

import java.sql.Timestamp;
import java.util.List;

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
    public List<ViewStats> getStatistic(Timestamp start, Timestamp end, List<String> uris, Boolean unique) {
        if ((uris == null || uris.size() == 0) && !unique) {
            return statisticRepository.findAllAfterStartAndBeforeEnd(start, end);
        } else if ((uris == null || uris.size() == 0) && unique) {
            return statisticRepository.findAllAfterStartAndBeforeEndWithUnique(start, end);
        } else if (!unique) {
            return statisticRepository.findAllAfterStartAndBeforeEndByUris(start, end, uris);
        } else return statisticRepository.findAllAfterStartAndBeforeEndByUrisWithUnique(start, end, uris);
    }
}
