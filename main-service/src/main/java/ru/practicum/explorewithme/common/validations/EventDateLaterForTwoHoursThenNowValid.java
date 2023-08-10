package ru.practicum.explorewithme.common.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateValidator.class)
public @interface EventDateLaterForTwoHoursThenNowValid {
    String message() default "The date and time for which the event is scheduled cannot be earlier than two hours from the current moment";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
