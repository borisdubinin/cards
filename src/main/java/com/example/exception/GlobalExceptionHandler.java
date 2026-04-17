package com.example.exception;

import com.example.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleEntityNotFound(EntityNotFoundException e) {
        log.debug("Entity wasn't found: %s".formatted(e.getMessage()));
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleIllegalArgument(IllegalArgumentException e) {
        log.debug("Illegal argument encountered: %s".formatted(e.getMessage()));
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("Illegal argument was received: %s".formatted(e.getMessage()));
        return new ErrorResponseDto(e.getMessage());
    }
}
