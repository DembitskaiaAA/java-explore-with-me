package ru.practicum.explorewithme.dto.compilationDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.dto.eventDto.EventShortDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Integer id;
    Set<EventShortDto> events;
    Boolean pinned;
    String title;

}
