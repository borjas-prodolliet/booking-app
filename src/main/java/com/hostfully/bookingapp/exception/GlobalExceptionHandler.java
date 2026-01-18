package com.hostfully.bookingapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String BAD_REQUEST = "BAD_REQUEST";

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<AppExceptionResponse> handleAppException(AppException e) {
        return new ResponseEntity<>(
                new AppExceptionResponse(e.getMessage()),
                e.getExceptionDetail().getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<AppExceptionResponse> handleInvalidArgumentsException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new AppExceptionResponse(BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<AppExceptionResponse> handleArgumentsConversionException(HttpMessageConversionException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new AppExceptionResponse(BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<AppExceptionResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new AppExceptionResponse(INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
