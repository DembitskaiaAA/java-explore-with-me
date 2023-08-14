package ru.practicum.explorewithme.dto.compilationDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    Set<Integer> events;
    Boolean pinned;
    @Size(min = 1, max = 50, message = "Length of the compilation must be between 1 and 50")
    String title;
}
