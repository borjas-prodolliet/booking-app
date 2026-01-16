package com.hostfully.bookingapp.property.domain;

import java.time.LocalDate;
import java.util.UUID;

public interface PropertyDao {
    Property get(UUID propertyId);

    boolean isAvailable(UUID propertyId, LocalDate dateFrom, LocalDate dateTo);
}
