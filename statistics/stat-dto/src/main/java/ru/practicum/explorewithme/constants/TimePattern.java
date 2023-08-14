package ru.practicum.explorewithme.constants;

import java.time.format.DateTimeFormatter;

public class TimePattern {
    public static final String DATATIMEPATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATATIMEPATTERN);

}
