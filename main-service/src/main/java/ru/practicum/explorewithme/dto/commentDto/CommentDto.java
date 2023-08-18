package ru.practicum.explorewithme.dto.commentDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.constants.TimePattern.DATATIMEPATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Integer id;
    String commentator;
    Integer event;
    String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATATIMEPATTERN)
    LocalDateTime created;
}
