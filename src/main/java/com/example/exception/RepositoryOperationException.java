package com.example.exception;

public class RepositoryOperationException extends RuntimeException {
    public RepositoryOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
