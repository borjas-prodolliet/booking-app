package com.bookingapp.block.application;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateBlockRequest(@NotNull @Future LocalDate dateFrom,
                                 @NotNull @Future LocalDate dateTo) {
}
