package ru.practicum.explorewithme.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.checks.CheckExist;
import ru.practicum.explorewithme.dto.userDto.NewUserRequest;
import ru.practicum.explorewithme.dto.userDto.UserDto;
import ru.practicum.explorewithme.exceptions.ConditionException;
import ru.practicum.explorewithme.mapper.userMapper.UserMapper;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CheckExist checkExist;

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        User user = userMapper.transformNewUserRequestToUser(newUserRequest);
        if (userRepository.findByName(newUserRequest.getName()) != null) {
            throw new ConditionException(String.format("User name %s already exists", newUserRequest.getName()));
        }
        User savedUser = userRepository.save(user);
        return userMapper.transformUserToUserDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, Pageable pageable) {
        if (ids != null) {
            return userRepository.findUsersByIds(ids, pageable).stream().map(x -> userMapper.transformUserToUserDto(x)).collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).stream().map(x -> userMapper.transformUserToUserDto(x)).collect(Collectors.toList());
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        checkExist.checkUserOnExist(userId, String.format("Error when deleting: user with id: %s is missing", userId));
        userRepository.deleteById(userId);
    }
}
