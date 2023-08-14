package ru.practicum.explorewithme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@RestController
@Slf4j
public class StatisticController {
    @Autowired
    private StatisticService statisticService;


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postStatistic(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST EndpointHit {}", endpointHitDto);
        statisticService.postStatistic(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStatistic(@RequestParam @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = DATATIMEPATTERN) LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info("GET stats1 start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        List<ViewStatsDto> statistic = statisticService.getStatistic(start, end, uris, unique);
        return statistic;
    }
}
