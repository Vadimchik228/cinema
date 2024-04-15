package com.vasche.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.vasche.util.constants.ApplicationProperties.*;

public class PropertiesUtilTest {
    public static Stream<Arguments> getArgumentsForTest() {
        return Stream.of(Arguments.of(URL_KEY, "jdbc:postgresql://localhost:5432/cinema"),
                Arguments.of(USER_KEY, "postgres"),
                Arguments.of(PASSWORD_KEY, "1"));
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForTest")
    void get(final String key, final String expectedResult) {
        Assertions.assertThat(PropertiesUtil.get(key))
                .isEqualTo(expectedResult);
    }
}
