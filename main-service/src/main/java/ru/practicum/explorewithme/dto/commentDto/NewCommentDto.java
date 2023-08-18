package ru.practicum.explorewithme.dto.commentDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentDto {
    @NotBlank(message = "Text of comment cannot be empty or null")
    @Size(min = 1, max = 2000, message = "Length of the comment of the event must be between 1 and 2000")
    String text;
}
