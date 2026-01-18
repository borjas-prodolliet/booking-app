package com.hostfully.bookingapp.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final AppExceptionDetail exceptionDetail;

    public AppException(AppExceptionDetail exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
        super(exceptionDetail.getMessage());
    }
}
