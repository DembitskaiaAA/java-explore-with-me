package ru.practicum.explorewithme.exceptions;

public class EndTimeInThePastException extends RuntimeException {
    public EndTimeInThePastException(String message) {
        super(message);
    }
}
