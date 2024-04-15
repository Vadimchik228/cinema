package com.vasche.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalInfoUtilTest {

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfFirstNames")
    void isValidFirstName(String firstname, boolean expectedResult) {
        boolean actualResult = PersonalInfoUtil.isCorrectFirstName(firstname);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArgumentsOfFirstNames() {
        return Stream.of(
                Arguments.of("Vadim", true),
                Arguments.of("Вадим", true),
                Arguments.of("вадим", false),
                Arguments.of("vadim", false),
                Arguments.of("Vad32im", false),
                Arguments.of("Вад23им", false),
                Arguments.of("", false),
                Arguments.of(" Вадим", true),
                Arguments.of("Вадим ", true),
                Arguments.of(" Вадим ", true),
                Arguments.of(" Вадим Щ", false),
                Arguments.of("Ян", true),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfLastNames")
    void isValidLastName(String lastname, boolean expectedResult) {
        boolean actualResult = PersonalInfoUtil.isCorrectLastName(lastname);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArgumentsOfLastNames() {
        return Stream.of(
                Arguments.of("Schebetovskiy", true),
                Arguments.of("Щебетовский", true),
                Arguments.of("Салтыков-Щедрин", true),
                Arguments.of(" Салтыков-Щедрин", true),
                Arguments.of("Салтыков-Щедрин ", true),
                Arguments.of(" Салтыков-Щедрин ", true),
                Arguments.of(" ", false),
                Arguments.of("Салтыков-Щедрин В", false),
                Arguments.of("Saltikov-Schedrin", true),
                Arguments.of("салтыков-Щедрин", false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfEmails")
    void isValidEmail(String email, boolean expectedResult) {
        boolean actualResult = PersonalInfoUtil.isCorrectEmail(email);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArgumentsOfEmails() {
        return Stream.of(
                Arguments.of("vadim@gmail.com", true),
                Arguments.of("lol228Vadim@email.ru", true),
                Arguments.of("lol228Vadim@email.", false),
                Arguments.of(" lol228Vadim@email.ru ", true),
                Arguments.of(" lol228Вадим@email.ru ", false),
                Arguments.of(" lol228Вадимemail.ru ", false),
                Arguments.of("lol228Vadim@emailru", false),
                Arguments.of("lol228Vadim@.ru", false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getValidationArgumentsOfPasswords")
    void isValidPassword(String password, boolean expectedResult) {
        boolean actualResult = PersonalInfoUtil.isCorrectPassword(password);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArgumentsOfPasswords() {
        return Stream.of(
                Arguments.of("vadimsdf", false),
                Arguments.of("V123!", false),
                Arguments.of("Vv123!", true),
                Arguments.of("1234234", false),
                Arguments.of("!#$%^&#", false),
                Arguments.of(" va2342! ", true),
                Arguments.of("ыыщщБ2342! ", true),
                Arguments.of(null, false)
        );
    }
}
