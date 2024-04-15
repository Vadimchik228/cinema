package com.vasche.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateTimeFormatterTest {
    @Test
    void format() {
        String date = "2020-11-28 16:30";

        LocalDateTime actualResult = LocalDateTimeFormatter.format(date);

        assertThat(actualResult).isEqualTo(LocalDateTime.of(2020, 11, 28, 16, 30));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String date, boolean expectedResult) {
        boolean actualResult = LocalDateTimeFormatter.isValid(date);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2020-11-28 15:23", true),
                Arguments.of("01-01-2001 15:30", false),
                Arguments.of("15:30 2020-11-28", false),
                Arguments.of("2020-11-28", false),
                Arguments.of("15:30", false),
                Arguments.of(null, false)
        );
    }
}
