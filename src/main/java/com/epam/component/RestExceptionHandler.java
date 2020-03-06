package com.epam.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
//                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
//        String errorMessage = exception.getBindingResult().getAllErrors().stream()
//                .map(objectError -> objectError.getDefaultMessage().concat(SEMICOLON))
//                .reduce(EMPTY, String::concat);
//        log.error(errorMessage, exception);
//        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
//        return new ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(value = {ConstraintViolationException.class})
//    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
//        String errorMessage = exception.getConstraintViolations().stream()
//                .map(constraintViolation -> constraintViolation.getMessage().concat(SEMICOLON))
//                .reduce(EMPTY, String::concat);
//        log.error(errorMessage, exception);
//        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
//        return handleExceptionInternal(exception, errorResponseDTO, new HttpHeaders(), errorResponseDTO.getHttpStatus(), request);
//    }
//
//    @ExceptionHandler(value = {RuntimeException.class})
//    protected ResponseEntity<Object> handleRuntimeException(RuntimeException exception, WebRequest request) {
//        log.error(exception.getMessage(), exception);
//        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
//        return handleExceptionInternal(exception, errorResponseDTO, new HttpHeaders(), errorResponseDTO.getHttpStatus(), request);
//    }

}
