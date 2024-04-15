package com.vasche.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PersonalInfoUtil {
    public static boolean isCorrectFirstName(String firstName) {
        if (firstName == null) return false;
        String trimFirstName = firstName.trim();
        String regex = "^[A-ZА-Я][a-zA-Zа-яА-Я]*$";
        return Pattern.matches(regex, trimFirstName);
    }

    public static boolean isCorrectLastName(String lastName) {
        if (lastName == null) return false;
        String trimLastName = lastName.trim();
        String regex = "^[А-ЯЁA-Z][а-яёa-zА-ЯЁA-Z\\-]*$";
        return Pattern.matches(regex, trimLastName);
    }

    public static boolean isCorrectEmail(String email) {
        if (email == null) return false;
        String trimEmail = email.trim();
        String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$";
        return Pattern.matches(regex, trimEmail);
    }

    public static boolean isCorrectPassword(String password) {
        if (password == null) return false;
        String trimPassword = password.trim();
        if (trimPassword.length() < 6) {
            return false;
        }

        // Проверяем наличие хотя бы одной цифры, буквы и символа из списка
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Zа-яА-Я])(?=.*[!\"№%*@?$&]).{6,}$";

        return Pattern.compile(regex).matcher(trimPassword).matches();
    }
}
