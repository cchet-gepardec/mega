package com.gepardec.mega.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Objects;

public class DateUtils {

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm", Locale.getDefault());
    private static final Locale GERMAN_LOCALE = Locale.GERMANY;

    public static LocalDate toLocalDate(String dateAsString) {
        return LocalDate.parse(dateAsString, DEFAULT_DATE_FORMATTER);
    }

    public static String dateToString(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }


    public static LocalDateTime toDateTime(String dateAsString, String timeAsString) {
        return LocalDateTime.parse(dateAsString + timeAsString, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * when we have to secure our tests with specific time, we could set it here
     *
     * @return
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    public static String getFirstDayOfFollowingMonth(String dateAsString) {
        Objects.requireNonNull(dateAsString, "Date must not be null!");
        return dateToString(
                toLocalDate(dateAsString)
                        .with(TemporalAdjusters.firstDayOfNextMonth()));
    }

    public static String getLastDayOfFollowingMonth(String dateAsString) {
        Objects.requireNonNull(dateAsString, "Date must not be null!");
        return dateToString(
                toLocalDate(dateAsString)
                        .plusMonths(1)
                        .with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static double calcDiffInHours(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() / 60.0;
    }

    public static String getDayByDate(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, GERMAN_LOCALE);
    }

}
