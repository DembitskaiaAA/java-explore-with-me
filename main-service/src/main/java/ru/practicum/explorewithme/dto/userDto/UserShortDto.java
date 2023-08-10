package ru.practicum.explorewithme.dto.userDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    @NotBlank(message = "User id cannot be empty")
    Integer id;
    @NotBlank(message = "User name cannot be empty")
    String name;
}
