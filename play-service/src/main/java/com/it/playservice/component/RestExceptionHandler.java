package com.it.playservice.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String SEMICOLON = ";";
    private static final String EMPTY = "";

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handle(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage().concat(SEMICOLON))
                .reduce(EMPTY, String::concat);
        log.error(errorMessage, exception);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handle(Exception exception, WebRequest request) {
        String errorMessage = exception.getMessage();
        log.error(errorMessage, exception);
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handle(RuntimeException exception, WebRequest request) {
        String errorMessage = exception.getMessage();
        log.error(errorMessage, exception);
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
