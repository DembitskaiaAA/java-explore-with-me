package ru.practicum.explorewithme.dto.compilationDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    Set<Integer> events;
    @Builder.Default
    Boolean pinned = false;
    @NotBlank(message = "Title of the compilation cannot be empty")
    @Size(min = 1, max = 50, message = "Length of the compilation must be between 1 and 50")
    String title;
}
