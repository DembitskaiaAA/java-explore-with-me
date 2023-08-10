package ru.practicum.explorewithme.dto.eventDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.dto.userDto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    Integer id;
    @NotBlank(message = "Brief description of the event cannot be empty")
    String annotation;

    @NotNull(message = "Category of the event cannot be empty")
    CategoryDto category;

    Integer confirmedRequests;

    @NotNull(message = "EventDate of the event cannot be empty")
    @JsonFormat(pattern = DATATIMEPATTERN)
    LocalDateTime eventDate;

    @NotNull(message = "Initiator of the event cannot be empty")
    UserShortDto initiator;

    @NotNull(message = "Information about payment of the event cannot be empty")
    Boolean paid;

    @NotBlank(message = "Title of the event cannot be empty")
    String title;

    Long views;
}
