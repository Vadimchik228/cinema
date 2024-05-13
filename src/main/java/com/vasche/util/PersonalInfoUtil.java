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

    /*
      Регулярное выражение состоит из следующих частей:
      1)  ^[a-zA-Z0-9_+&*-]+: Эта часть проверяет, что адрес электронной почты начинается с одного или нескольких букв,
          цифр, знаков подчеркивания, плюсов, минусов или звездочек.
      2)  (?:.[a-zA-Z0-9_+&-]+): Эта часть проверяет, что адрес электронной почты может содержать один или несколько точек,
          за которыми следуют еще буквы, цифры, знаки подчеркивания, плюсы, минусы или звездочки.
      3)  @: Эта часть проверяет, что адрес электронной почты содержит символ @.
      4)  (?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,7}$: Эта часть проверяет, что адрес электронной почты содержит доменное имя,
          которое состоит из одного или нескольких частей, разделенных точками.
          Каждая часть доменного имени может содержать буквы, цифры или дефисы.
          Последняя часть доменного имени должна состоять из 2-7 букв.
     */

    public static boolean isCorrectEmail(String email) {
        if (email == null) return false;
        String trimEmail = email.trim();
        String regex = "^[a-zA-Z0-9_+&*-]+" +
                       "(?:\\.[a-zA-Z0-9_+&*-]+)" +
                       "*@" +
                       "(?:[a-zA-Z0-9-:/]+\\.)+[a-zA-Z]{2,7}$";
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
