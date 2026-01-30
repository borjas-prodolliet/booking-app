package com.bookingapp.property.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PropertyDao {
    Optional<Property> get(UUID propertyId);

    Boolean hasBookingInDateRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID bookingId);

    Boolean hasBlockInDateRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID blockId);
}
