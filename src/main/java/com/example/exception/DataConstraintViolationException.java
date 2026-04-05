package com.example.exception;

public class DataConstraintViolationException extends RuntimeException {
    public DataConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
