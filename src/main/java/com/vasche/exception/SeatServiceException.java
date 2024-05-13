package com.vasche.exception;

public class SeatServiceException extends RuntimeException {
    public SeatServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
