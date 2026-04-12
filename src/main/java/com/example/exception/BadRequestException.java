package com.example.exception;

@Deprecated
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
