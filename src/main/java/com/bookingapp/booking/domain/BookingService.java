package com.bookingapp.booking.domain;

import com.bookingapp.booking.application.CreateBookingRequest;
import com.bookingapp.booking.application.UpdateBookingRequest;
import com.bookingapp.exception.AppException;
import com.bookingapp.exception.AppExceptionDetail;
import com.bookingapp.property.domain.Property;
import com.bookingapp.property.domain.PropertyService;
import com.bookingapp.user.domain.User;
import com.bookingapp.user.domain.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.bookingapp.shared.DateValidator.validateDateRange;

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

        validateDateRange(request.dateFrom(), request.dateTo());

        Booking booking = getBooking(bookingId);

        if (Boolean.TRUE.equals(booking.getCanceled())) {
            log.error("Cannot update canceled booking");
            throw new AppException(AppExceptionDetail.BOOKING_ALREADY_CANCELLED);
        }

        propertyService.verifyPropertyAvailability(
                booking.getProperty().getId(), request.dateFrom(), request.dateTo(), bookingId, null);

        boolean hasChanges = false;

        if (booking.getMessage() != null && !booking.getMessage().equals(request.message()) ||
                booking.getMessage() == null && request.message() != null) {
            hasChanges = true;
            booking.setMessage(request.message());
        }

        if (!booking.getDateFrom().equals(request.dateFrom())) {
            hasChanges = true;
            booking.setDateFrom(request.dateFrom());
        }

        if (!booking.getDateTo().equals(request.dateTo())) {
            hasChanges = true;
            booking.setDateTo(request.dateTo());
        }

        if (!booking.getAdults().equals(request.adults())) {
            hasChanges = true;
            booking.setAdults(request.adults());
        }

        if (!booking.getChildren().equals(request.children())) {
            hasChanges = true;
            booking.setChildren(request.children());
        }

        if (!hasChanges) {
            log.error("The booking has no changes");
            throw new AppException(AppExceptionDetail.NO_CHANGES);
        }

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
