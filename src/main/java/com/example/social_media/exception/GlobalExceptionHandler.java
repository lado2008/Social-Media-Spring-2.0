package com.example.social_media.exception;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ExceptionBody> handleMethondArgumentException(MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        List<FieldError> fieldErrors = exception.getFieldErrors();
        ExceptionBody exceptionBody = new ExceptionBody();
        List<String> messages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            messages.add(fieldError.getDefaultMessage());
        }
        exceptionBody.setMessage(messages);
        exceptionBody.setTimestamp(System.currentTimeMillis());
        exceptionBody.setEndpoint(request.getRequestURI());
        exceptionBody.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionBody);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionBody> handleResponseStatusException(ResponseStatusException exception,
            HttpServletRequest request) {
        ExceptionBody exceptionBody = new ExceptionBody();
        exceptionBody.setMessage(List.of(exception.getMessage()));
        exceptionBody.setTimestamp(System.currentTimeMillis());
        exceptionBody.setEndpoint(request.getRequestURI());
        exceptionBody.setStatusCode(exception.getStatusCode().value());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionBody);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionBody> handleRuntimeException(RuntimeException exception,
            HttpServletRequest request) {
        ExceptionBody exceptionBody = new ExceptionBody();
        exceptionBody.setMessage(List.of(exception.getMessage()));
        exceptionBody.setTimestamp(System.currentTimeMillis());
        exceptionBody.setEndpoint(request.getRequestURI());
        exceptionBody.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }
}