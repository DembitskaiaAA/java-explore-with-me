package ru.practicum.explorewithme.mapper.eventMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.dto.eventDto.EventFullDto;
import ru.practicum.explorewithme.dto.eventDto.EventShortDto;
import ru.practicum.explorewithme.dto.eventDto.NewEventDto;
import ru.practicum.explorewithme.dto.userDto.UserShortDto;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

@Mapper(componentModel = "spring")
@Component
public interface EventMapper {
    @Mapping(target = "category", ignore = true)
    Event transformNewEventDtoToEvent(NewEventDto newEventDto);

    EventFullDto transformEventToEventFullDto(Event event);

    default UserShortDto userToUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    EventShortDto transformEventToEventShortDto(Event event);

    CategoryDto transformCategoryToCategoryDto(Category category);

}
