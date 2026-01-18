package com.hostfully.bookingapp.booking.application;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateBookingRequest(@NotNull UUID propertyId, @NotNull UUID mainGuestId,
                                   @NotNull @Future LocalDate dateFrom,
                                   @NotNull @Future LocalDate dateTo, String message,
                                   @NotNull @Min(1) Integer adults, @NotNull Integer children) {
}
