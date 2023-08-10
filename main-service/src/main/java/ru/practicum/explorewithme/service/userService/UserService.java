package ru.practicum.explorewithme.service.userService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.userDto.NewUserRequest;
import ru.practicum.explorewithme.dto.userDto.UserDto;

import java.util.List;

@Service
public interface UserService {
    UserDto postUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Integer> ids, Pageable pageable);

    void deleteUser(Integer userId);
}
