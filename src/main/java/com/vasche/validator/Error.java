package com.vasche.validator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class Error {
    private final String code;

    private final String message;

    private Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Error of(String code, String message) {
        return new Error(code, message);
    }

}
