package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

@Component
@Mapper(componentModel = "spring")
public interface StatMapper {
    EndpointHit transformEndpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);

    EndpointHitDto transformEndpointHitToEndpointHitDto(EndpointHit endpointHit);

    ViewStats transformViewStatsDtoToViewStats(ViewStatsDto viewStatsDto);

    ViewStatsDto transformViewStatsToViewStatsDto(ViewStats viewStats);
}
