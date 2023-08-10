package ru.practicum.explorewithme.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.userDto.NewUserRequest;
import ru.practicum.explorewithme.dto.userDto.UserDto;
import ru.practicum.explorewithme.service.userService.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/users")
public class AdminControllerUsers {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("POST /admin/users BODY NewUserRequest {}", newUserRequest);
        return userService.postUser(newUserRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /admin/users PARAMS ids {}, from {}, size {}", ids, from, size);
        return userService.getUsers(ids, Pagination.splitByPages(from, size));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("DELETE /admin/users/{} ", userId);
        userService.deleteUser(userId);
    }
}
