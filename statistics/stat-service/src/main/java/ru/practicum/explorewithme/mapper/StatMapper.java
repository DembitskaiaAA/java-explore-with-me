package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

@Component
@Mapper(componentModel = "spring")
public abstract class StatMapper {
    public abstract EndpointHit transformEndpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);

    public abstract EndpointHitDto transformEndpointHitToEndpointHitDto(EndpointHit endpointHit);

    public abstract ViewStats transformViewStatsDtoToViewStats(ViewStatsDto viewStatsDto);

    public abstract ViewStatsDto transformViewStatsToViewStatsDto(ViewStats viewStats);
}
