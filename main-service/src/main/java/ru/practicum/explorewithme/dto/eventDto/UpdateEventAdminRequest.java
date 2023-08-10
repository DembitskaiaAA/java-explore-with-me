package ru.practicum.explorewithme.dto.eventDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.common.enums.TypesEventsAdminResponses;
import ru.practicum.explorewithme.common.validations.EventDateLaterForTwoHoursThenNowValid;
import ru.practicum.explorewithme.model.Location;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "Length of brief description of the event must be between 20 and 2000")
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000, message = "Length of description of the event must be between 20 and 7000")
    String description;

    @JsonFormat(pattern = DATATIMEPATTERN)
    @EventDateLaterForTwoHoursThenNowValid
    @FutureOrPresent(message = "Event date cannot be in the past")
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    @PositiveOrZero(message = "The maximum number of event participants cannot be negative")
    Integer participantLimit;

    Boolean requestModeration;

    TypesEventsAdminResponses stateAction;

    @Size(min = 3, max = 120, message = "Length of title of the event must be between 3 and 120")
    String title;
}
