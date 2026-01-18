package com.hostfully.bookingapp.block.application;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateBlockRequest(@NotNull UUID propertyId,
                                 @NotNull @Future LocalDate dateFrom,
                                 @NotNull @Future LocalDate dateTo) {
}
