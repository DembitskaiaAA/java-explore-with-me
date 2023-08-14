package ru.practicum.explorewithme;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {
    Integer id;
    @NotEmpty(message = "Service ID cannot be empty")
    @Size(max = 100, message = "The maximum size of a service ID is 100 characters")
    String app;

    @NotEmpty(message = "Request URI cannot be empty")
    String uri;

    @NotEmpty(message = "User IP cannot be empty")
    @Size(max = 20, message = "User IP cannot be more than 20 characters")
    String ip;

    @NotNull(message = "Request time cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATATIMEPATTERN)
    LocalDateTime timestamp;
}