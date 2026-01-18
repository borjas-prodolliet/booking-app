package com.hostfully.bookingapp.booking.application;

import com.hostfully.bookingapp.booking.domain.Booking;
import com.hostfully.bookingapp.booking.domain.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @PutMapping(value = "/{bookingId}")
    public ResponseEntity<Booking> updateBooking(@PathVariable UUID bookingId, @RequestBody @Valid UpdateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(bookingId, request));
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBooking(bookingId));
    }

    @PatchMapping(value = "/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.cancelBooking(bookingId));
    }

    @PatchMapping(value = "/{bookingId}/rebook")
    public ResponseEntity<Booking> rebookCanceledBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.rebookCanceledBooking(bookingId));
    }

    @DeleteMapping(value = "/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
