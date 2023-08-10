package ru.practicum.explorewithme.mapper.userMapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.userDto.NewUserRequest;
import ru.practicum.explorewithme.dto.userDto.UserDto;
import ru.practicum.explorewithme.model.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User transformNewUserRequestToUser(NewUserRequest newUserRequest);

    UserDto transformUserToUserDto(User user);
}