package com.hostfully.bookingapp.booking.application;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateBookingRequest(@NotNull @Future LocalDate dateFrom,
                                   @NotNull @Future LocalDate dateTo, String message,
                                   @NotNull Integer adults, @NotNull Integer children) {
}
