package com.bookingapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppExceptionDetail {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"),
    PROPERTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PROPERTY_NOT_FOUND"),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "INVALID_DATE_RANGE"),
    PROPERTY_NOT_AVAILABLE(HttpStatus.CONFLICT, "PROPERTY_NOT_AVAILABLE"),
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING_NOT_FOUND"),
    BOOKING_ALREADY_CANCELLED(HttpStatus.CONFLICT, "BOOKING_ALREADY_CANCELLED"),
    BOOKING_NOT_CANCELLED(HttpStatus.CONFLICT, "BOOKING_NOT_CANCELLED"),
    NO_CHANGES(HttpStatus.NOT_MODIFIED, "NO_CHANGES"),
    BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "BLOCK_NOT_FOUND");

    private final HttpStatus httpStatus;
    private final String message;

    AppExceptionDetail(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
