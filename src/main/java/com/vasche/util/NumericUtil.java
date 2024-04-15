package com.vasche.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class NumericUtil {
    public static boolean isPositiveNumber(String str) {
        if (str == null) {
            return false;
        }

        try {
            int number = Integer.parseInt(str);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isMinimumAge(String str) {
        if (str == null) {
            return false;
        }

        try {
            int number = Integer.parseInt(str);
            return number == 0 || number == 6 || number == 12 || number == 16 || number == 18;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPrice(String str) {
        return str != null && isPositiveBigDecimal(str);
    }

    private static boolean isPositiveBigDecimal(String str) {
        try {
            BigDecimal number = new BigDecimal(str);
            return number.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
