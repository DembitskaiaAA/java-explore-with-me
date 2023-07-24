package ru.practicum.explorewithme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.service.StatisticService;

import java.sql.Timestamp;
import java.util.List;

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
    public List<ViewStats> getStatistic(@RequestParam Timestamp start,
                                        @RequestParam Timestamp end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info("GET stats start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statisticService.getStatistic(start, end, uris, unique);
    }
}
