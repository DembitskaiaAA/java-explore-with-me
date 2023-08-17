package ru.practicum.explorewithme.dto.eventDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.common.enums.PublishingStatus;
import ru.practicum.explorewithme.dto.userDto.UserShortDto;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Set;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Integer id;
    @NotEmpty(message = "Brief description of the event cannot be empty")
    String annotation;

    @NotNull(message = "Category of the event cannot be empty")
    Category category;

    Integer confirmedRequests;

    @JsonFormat(pattern = DATATIMEPATTERN)
    LocalDateTime createdOn;

    String description;

    @NotNull(message = "EventDate of the event cannot be empty")
    @JsonFormat(pattern = DATATIMEPATTERN)
    LocalDateTime eventDate;

    @NotNull(message = "Initiator of the event cannot be empty")
    UserShortDto initiator;

    @NotNull(message = "Location of the event cannot be empty")
    Location location;

    @NotNull(message = "Information about payment of the event cannot be empty")
    boolean paid;

    @PositiveOrZero(message = "The maximum number of event participants cannot be negative")
    int participantLimit;

    @JsonFormat(pattern = DATATIMEPATTERN)
    LocalDateTime publishedOn;

    boolean requestModeration;

    PublishingStatus state;

    @NotEmpty(message = "Title of the event cannot be empty")
    String title;

    Set<Comment> comments;

    Long views;
}
