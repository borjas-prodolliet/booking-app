package com.hostfully.bookingapp.shared;

import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class DateValidator {
    private DateValidator() {
    }

    public static void validateDateRange(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom.isEqual(dateTo) || dateTo.isBefore(dateFrom)) {
            log.error("Date to cannot be equal or before date from");
            throw new AppException(AppExceptionDetail.INVALID_DATE_RANGE);
        }
    }
}
