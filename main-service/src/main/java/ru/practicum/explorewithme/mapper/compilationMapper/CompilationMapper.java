package ru.practicum.explorewithme.mapper.compilationMapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.compilationDto.CompilationDto;
import ru.practicum.explorewithme.dto.eventDto.EventShortDto;
import ru.practicum.explorewithme.dto.userDto.UserShortDto;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

@Component
@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationDto transformCompilationToCompilationDto(Compilation compilation);

    default UserShortDto userToUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    EventShortDto eventToEventShortDto(Event event);
}
