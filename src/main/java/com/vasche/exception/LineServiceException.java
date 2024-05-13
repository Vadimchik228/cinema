package com.vasche.exception;

public class LineServiceException extends RuntimeException {
    public LineServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}