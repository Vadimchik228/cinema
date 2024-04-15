package com.vasche.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {

    private static final String FORMAT = "\\WEB-INF\\jsp\\%s.jsp";

    public static String get(String path) {
        return String.format(FORMAT, path);
    }
}
