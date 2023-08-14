package ru.practicum.explorewithme.dto.requestDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateResult {
    Set<ParticipationRequestDto> confirmedRequests;
    Set<ParticipationRequestDto> rejectedRequests;
}
