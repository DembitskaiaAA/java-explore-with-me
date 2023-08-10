package ru.practicum.explorewithme.common.validations;

import ru.practicum.explorewithme.dto.eventDto.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<EventDateLaterForTwoHoursThenNowValid, NewEventDto> {

    @Override
    public void initialize(EventDateLaterForTwoHoursThenNowValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(NewEventDto newEventDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime nowPlusTwoHours = LocalDateTime.now().plusHours(2);
        LocalDateTime eventDate = newEventDto.getEventDate();
        return eventDate.isAfter(nowPlusTwoHours);
    }
}
