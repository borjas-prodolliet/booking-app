package com.hostfully.bookingapp.shared;

import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateValidatorTest {

    @Test
    @DisplayName("Given a valid date range, should validate success")
    void validateDateRangeSuccess() {
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        DateValidator.validateDateRange(dateFrom, dateTo);
    }

    @Test
    @DisplayName("Given equal dates in range, should throw an exception")
    void validateDateRangeEqualShouldThrowException() {
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 21);

        assertThatThrownBy(() -> DateValidator.validateDateRange(dateFrom, dateTo))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.INVALID_DATE_RANGE.getMessage());
    }

    @Test
    @DisplayName("Given dateFrom after dateTo, should throw an exception")
    void validateIncorrectDateRangeThrowsException() {
        LocalDate dateFrom = LocalDate.of(2026, 10, 25);
        LocalDate dateTo = LocalDate.of(2026, 10, 21);

        assertThatThrownBy(() -> DateValidator.validateDateRange(dateFrom, dateTo))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.INVALID_DATE_RANGE.getMessage());
    }
}