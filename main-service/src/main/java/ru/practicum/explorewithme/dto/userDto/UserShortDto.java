package ru.practicum.explorewithme.dto.userDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    @NotEmpty(message = "User id cannot be empty")
    Integer id;
    @NotEmpty(message = "User name cannot be empty")
    String name;
}
