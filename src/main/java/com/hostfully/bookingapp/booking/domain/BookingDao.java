package com.hostfully.bookingapp.booking.domain;

import java.util.Optional;
import java.util.UUID;

public interface BookingDao {
    Booking create(Booking booking);

    Booking update(Booking booking);

    void delete(Booking booking);

    Optional<Booking> get(UUID bookingId);
}
