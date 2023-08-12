package ru.practicum.explorewithme.dto.eventDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.common.validations.EventDateLaterForTwoHoursThenNowValid;
import ru.practicum.explorewithme.model.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotEmpty(message = "Brief description of the event cannot be empty")
    @Size(min = 20, max = 2000, message = "Length of brief description of the event must be between 20 and 2000")
    String annotation;

    @NotNull(message = "Category of the event cannot be empty")
    Integer category;

    @NotEmpty(message = "Description of the event cannot be empty")
    @Size(min = 20, max = 7000, message = "Length of description of the event must be between 20 and 7000")
    String description;

    @NotNull(message = "EventDate of the event cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATATIMEPATTERN)
    @EventDateLaterForTwoHoursThenNowValid
    @FutureOrPresent(message = "Event date cannot be in the past")
    LocalDateTime eventDate;

    @NotNull(message = "Location of the event cannot be empty")
    Location location;

    boolean paid;

    @PositiveOrZero(message = "The maximum number of event participants cannot be negative")
    int participantLimit;

    @Builder.Default
    boolean requestModeration = true;

    @NotEmpty(message = "Title of the event cannot be empty")
    @Size(min = 3, max = 120, message = "Length of title of the event must be between 3 and 120")
    String title;
}
