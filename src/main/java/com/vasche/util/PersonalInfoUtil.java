package com.vasche.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PersonalInfoUtil {
    public static boolean isCorrectName(String firstName) {
        if (firstName == null) return false;
        String trimFirstName = firstName.trim();
        String regex = "^[А-ЯЁA-Z][а-яёa-zА-ЯЁA-Z\\-]*$";
        return Pattern.matches(regex, trimFirstName);
    }

    public static boolean isCorrectEmail(String email) {
        if (email == null) return false;
        String trimEmail = email.trim();
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                       "[a-zA-Z0-9_+&*-]+)*@" +
                       "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                       "A-Z]{2,7}$";
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
