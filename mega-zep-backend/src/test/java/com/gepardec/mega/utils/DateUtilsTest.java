package com.gepardec.mega.utils;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateUtilsTest {

    @Test
    void getLastDayOfFollowingMonth_normalDate_returnLastDayOfMonth() {
        assertEquals("2017-12-31", DateUtils.getLastDayOfFollowingMonth("2017-11-10"));
    }

    @Test
    void getLastDayOfFollowingMonth_decemberDate_returnLastDayOfMonthJanuary() {
        assertEquals("2018-01-31", DateUtils.getLastDayOfFollowingMonth("2017-12-01"));
    }

    @Test
    void getLastDayOfFollowingMonth_nullString_throwException() {
        assertThrows(NullPointerException.class, () -> DateUtils.getLastDayOfFollowingMonth(null));
    }

    @Test
    void getLastDayOfFollowingMonth_emptyDate_returnLastDayOfMonthJanuary() {
        assertThrows(DateTimeParseException.class, () -> DateUtils.getLastDayOfFollowingMonth(""));
    }


    @Test
    void getFirstDayOfFollowingMonth_normalDate_returnFirstDayOfNextMonth() {
        assertEquals("2019-02-01", DateUtils.getFirstDayOfFollowingMonth("2019-01-31"));
    }

    @Test
    void getFirstDayOfFollowingMonth_decemberDate_returnJanuaryDate() {
        assertEquals("2020-01-01", DateUtils.getFirstDayOfFollowingMonth("2019-12-01"));
    }


    @Test
    void getFirstDayOfFollowingMonth_nullString_throwException() {
        assertThrows(NullPointerException.class, () -> DateUtils.getFirstDayOfFollowingMonth(null));
    }

    @Test
    void getFirstDayOfFollowingMonth_emptyDate_returnLastDayOfMonthJanuary() {
        assertThrows(DateTimeParseException.class, () -> DateUtils.getLastDayOfFollowingMonth(""));
    }


}
