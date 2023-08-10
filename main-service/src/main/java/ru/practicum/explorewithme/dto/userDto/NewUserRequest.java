package ru.practicum.explorewithme.dto.userDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @Email
    @NotBlank(message = "Email cannot be empty")
    @Size(min = 6, max = 254, message = "User email length must be between 6 and 254")
    String email;

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 2, max = 250, message = "User name length must be between 2 and 250")
    String name;
}
