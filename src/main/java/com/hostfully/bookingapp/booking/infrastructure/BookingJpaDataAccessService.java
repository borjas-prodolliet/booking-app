package com.hostfully.bookingapp.booking.infrastructure;

import com.hostfully.bookingapp.booking.domain.Booking;
import com.hostfully.bookingapp.booking.domain.BookingDao;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookingJpaDataAccessService implements BookingDao {

    private final BookingRepository bookingRepository;

    public BookingJpaDataAccessService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    @Override
    public Optional<Booking> get(UUID bookingId) {
        return bookingRepository.findById(bookingId);
    }
}
