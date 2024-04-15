package com.vasche.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class NumericUtilTest {

    @Test
    void isInvalidMinimumAge() {
        String age = "5";
        assertFalse(NumericUtil.isMinimumAge(age));
    }

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfPositiveNumbers")
    void isValidPositiveNumber(String number, boolean expectedResult) {
        boolean actualResult = NumericUtil.isPositiveNumber(number);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArgumentsOfPositiveNumbers() {
        return Stream.of(
                Arguments.of("12", true),
                Arguments.of("0", false),
                Arguments.of("-200", false),
                Arguments.of("300.34", false),
                Arguments.of("", false),
                Arguments.of("letter", false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfPrices")
    void isValidPrice(String number, boolean expectedResult) {
        boolean actualResult = NumericUtil.isPrice(number);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArgumentsOfPrices() {
        return Stream.of(
                Arguments.of("1000", true),
                Arguments.of("0", false),
                Arguments.of("-1000", false),
                Arguments.of("300.34", true),
                Arguments.of(null, false)
        );
    }

}
