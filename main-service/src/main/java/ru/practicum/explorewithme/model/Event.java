package ru.practicum.explorewithme.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.common.enums.PublishingStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank
    String annotation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id")
    Category category;
    @Builder.Default
    Integer confirmedRequests = 0;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATATIMEPATTERN)
    LocalDateTime createdOn;

    @NotBlank
    String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATATIMEPATTERN)
    LocalDateTime eventDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "initiator", referencedColumnName = "id")
    User initiator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location", referencedColumnName = "id")
    Location location;

    @Builder.Default
    Boolean paid = false;

    @Builder.Default
    Integer participantLimit = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATATIMEPATTERN)
    LocalDateTime publishedOn;

    @Builder.Default
    Boolean requestModeration = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    PublishingStatus state;

    @NotBlank
    String title;
    @Builder.Default
    Long views = 0L;
}
