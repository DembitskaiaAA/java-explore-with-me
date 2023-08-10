package ru.practicum.explorewithme.mapper.requestMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.requestDto.ParticipationRequestDto;
import ru.practicum.explorewithme.model.Request;

@Component
@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    ParticipationRequestDto transformRequestToParticipationRequestDto(Request request);
}
