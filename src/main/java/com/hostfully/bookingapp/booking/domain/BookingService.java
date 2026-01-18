package com.hostfully.bookingapp.booking.domain;

import com.hostfully.bookingapp.booking.application.CreateBookingRequest;
import com.hostfully.bookingapp.booking.application.UpdateBookingRequest;
import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import com.hostfully.bookingapp.property.domain.Property;
import com.hostfully.bookingapp.property.domain.PropertyService;
import com.hostfully.bookingapp.user.domain.User;
import com.hostfully.bookingapp.user.domain.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.hostfully.bookingapp.shared.DateValidator.validateDateRange;

@Service
@Slf4j
public class BookingService {

    private final BookingDao bookingDao;
    private final UserService userService;
    private final PropertyService propertyService;

    public BookingService(BookingDao bookingDao, UserService userService, PropertyService propertyService) {
        this.bookingDao = bookingDao;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    public Booking createBooking(CreateBookingRequest request) {
        log.info("Creating booking {}", request);

        validateDateRange(request.dateFrom(), request.dateTo());

        propertyService.verifyPropertyAvailability(
                request.propertyId(), request.dateFrom(), request.dateTo(), null, null);

        User mainGuest = userService.getUser(request.mainGuestId());
        Property property = propertyService.getProperty(request.propertyId());

        Booking booking = Booking.builder()
                .mainGuest(mainGuest)
                .property(property)
                .dateFrom(request.dateFrom())
                .dateTo(request.dateTo())
                .message(request.message())
                .adults(request.adults())
                .children(request.children())
                .canceled(false)
                .build();

        return bookingDao.create(booking);
    }

    public Booking updateBooking(UUID bookingId, UpdateBookingRequest request) {
        log.info("Updating booking {}", bookingId);

        Booking booking = getBooking(bookingId);

        if (Boolean.TRUE.equals(booking.getCanceled())) {
            log.error("Cannot update canceled booking");
            throw new AppException(AppExceptionDetail.BOOKING_ALREADY_CANCELLED);
        }

        propertyService.verifyPropertyAvailability(
                booking.getProperty().getId(), request.dateFrom(), request.dateTo(), bookingId, null);

        booking.setDateFrom(request.dateFrom());
        booking.setDateTo(request.dateTo());
        booking.setMessage(request.message());
        booking.setAdults(request.adults());
        booking.setChildren(request.children());

        return bookingDao.update(booking);
    }

    public Booking cancelBooking(UUID bookingId) {
        log.info("Canceling booking {}", bookingId);

        Booking booking = getBooking(bookingId);

        if (Boolean.TRUE.equals(booking.getCanceled())) {
            log.error("Booking already cancelled");
            throw new AppException(AppExceptionDetail.BOOKING_ALREADY_CANCELLED);
        }

        booking.setCanceled(true);
        return bookingDao.update(booking);
    }

    public Booking rebookCanceledBooking(UUID bookingId) {
        log.info("Rebooking canceled booking {}", bookingId);

        Booking booking = getBooking(bookingId);

        if (Boolean.FALSE.equals(booking.getCanceled())) {
            log.error("Booking must be cancelled for rebooking");
            throw new AppException(AppExceptionDetail.BOOKING_NOT_CANCELLED);
        }

        booking.setCanceled(false);
        return bookingDao.update(booking);
    }

    public Booking getBooking(UUID bookingId) {
        log.info("Getting booking {}", bookingId);

        return bookingDao.get(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking not found: {}", bookingId);
                    return new AppException(AppExceptionDetail.BOOKING_NOT_FOUND);
                });
    }

    public void deleteBooking(UUID bookingId) {
        log.info("Deleting booking {}", bookingId);

        Booking booking = getBooking(bookingId);
        bookingDao.delete(booking);
    }
}
