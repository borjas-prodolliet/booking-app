package com.hostfully.bookingapp.booking.domain;

import com.hostfully.bookingapp.booking.application.CreateBookingRequest;
import com.hostfully.bookingapp.booking.application.UpdateBookingRequest;
import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import com.hostfully.bookingapp.property.domain.Property;
import com.hostfully.bookingapp.property.domain.PropertyService;
import com.hostfully.bookingapp.user.domain.LegalIdType;
import com.hostfully.bookingapp.user.domain.User;
import com.hostfully.bookingapp.user.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingDao bookingDao;
    @Mock
    private UserService userService;
    @Mock
    private PropertyService propertyService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        this.bookingService = new BookingService(bookingDao, userService, propertyService);
    }

    @Test
    @DisplayName("Given a correct create booking request, should create a booking")
    void createBookingSuccess() {
        UUID mainGuestId = UUID.randomUUID();
        UUID propertyId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);
        String message = "Some message";

        Property property = Property.builder()
                .id(propertyId)
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .nightlyPrice(100.0)
                .build();

        User user = User.builder()
                .id(mainGuestId)
                .email("email@example.com")
                .firstName("Jon")
                .lastName("Snow")
                .legalId("12341234")
                .legalIdType(LegalIdType.NATIONAL_ID)
                .build();

        Booking expected = Booking.builder()
                .property(property)
                .mainGuest(user)
                .dateFrom(dateFrom)
                .message(message)
                .dateTo(dateTo)
                .canceled(false)
                .adults(2)
                .children(1)
                .build();

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        when(userService.getUser(mainGuestId)).thenReturn(user);
        when(propertyService.getProperty(propertyId)).thenReturn(property);

        CreateBookingRequest request = new CreateBookingRequest(propertyId, mainGuestId, dateFrom, dateTo, message, 2, 1);

        bookingService.createBooking(request);

        verify(bookingDao).create(bookingCaptor.capture());

        assertEquals(expected, bookingCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("provideUpdateBookingFields")
    @DisplayName("Given a correct update booking request, should update a booking")
    void updateBookingSuccess(LocalDate dateFrom, LocalDate dateTo, String message, int adults, int children) {
        UUID bookingId = UUID.randomUUID();

        Property property = Property.builder()
                .id(UUID.randomUUID())
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .nightlyPrice(100.0)
                .build();

        Booking booking = Booking.builder()
                .id(bookingId)
                .property(property)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(false)
                .adults(2)
                .children(1)
                .build();

        Booking expected = Booking.builder()
                .id(bookingId)
                .property(property)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .message(message)
                .canceled(false)
                .adults(adults)
                .children(children)
                .build();

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));
        when(bookingDao.update(any(Booking.class))).thenReturn(expected);

        UpdateBookingRequest request = new UpdateBookingRequest(dateFrom, dateTo, message, adults, children);

        bookingService.updateBooking(bookingId, request);
        verify(bookingDao).update(bookingCaptor.capture());
        assertEquals(expected, bookingCaptor.getValue());
    }

    private static Stream<Arguments> provideUpdateBookingFields() {
        return Stream.of(
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 25), "Different message", 2, 1),
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 25), null, 2, 1),
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 25), "Some message", 2, 0),
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 25), "Some message", 3, 1),
                Arguments.of(LocalDate.of(2026, 10, 23), LocalDate.of(2026, 10, 25), "Some message", 2, 1),
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 23), "Some message", 2, 1)
        );
    }

    @Test
    @DisplayName("Given an updated booking request, when its already cancelled, should throw an exception")
    void updateBookingThrowsExceptionWhenItsCanceled() {
        UUID bookingId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        Property property = Property.builder()
                .id(UUID.randomUUID())
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .nightlyPrice(100.0)
                .build();

        Booking booking = Booking.builder()
                .id(bookingId)
                .property(property)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .canceled(true)
                .adults(2)
                .children(1)
                .build();

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        UpdateBookingRequest request = new UpdateBookingRequest(dateFrom, dateTo, null, 3, 1);

        assertThatThrownBy(() -> bookingService.updateBooking(bookingId, request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.BOOKING_ALREADY_CANCELLED.getMessage());
    }

    @Test
    @DisplayName("Given an updated booking request, when has no changes, should throw an exception")
    void updateBookingThrowsExceptionWhenNoChanges() {
        UUID bookingId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);
        String message = "Some message";

        Property property = Property.builder()
                .id(UUID.randomUUID())
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .nightlyPrice(100.0)
                .build();

        Booking booking = Booking.builder()
                .id(bookingId)
                .property(property)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .message(message)
                .id(UUID.randomUUID())
                .canceled(true)
                .adults(2)
                .children(1)
                .build();

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        UpdateBookingRequest request = new UpdateBookingRequest(dateFrom, dateTo, message, 2, 1);

        assertThatThrownBy(() -> bookingService.updateBooking(bookingId, request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.BOOKING_ALREADY_CANCELLED.getMessage());
    }

    @Test
    @DisplayName("When cancelling a booking, it should cancel the booking")
    void cancelBookingSuccess() {
        UUID bookingId = UUID.randomUUID();

        Booking booking = Booking.builder()
                .id(bookingId)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(false)
                .adults(2)
                .children(1)
                .build();

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(bookingId);

        verify(bookingDao).update(bookingCaptor.capture());

        assertTrue(bookingCaptor.getValue().getCanceled());
    }

    @Test
    @DisplayName("When cancelling a booking that is already canceled, it should throw an exception")
    void cancelBookingThrowsExceptionWhenAlreadyCanceled() {
        UUID bookingId = UUID.randomUUID();

        Booking booking = Booking.builder()
                .id(bookingId)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(true)
                .adults(2)
                .children(1)
                .build();


        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(bookingId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.BOOKING_ALREADY_CANCELLED.getMessage());
    }

    @Test
    @DisplayName("When rebooking a booking, it should rebook the booking")
    void rebookCanceledBookingSuccess() {
        UUID bookingId = UUID.randomUUID();

        Booking booking = Booking.builder()
                .id(bookingId)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(true)
                .adults(2)
                .children(1)
                .build();

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        bookingService.rebookCanceledBooking(bookingId);

        verify(bookingDao).update(bookingCaptor.capture());

        assertFalse(bookingCaptor.getValue().getCanceled());
    }

    @Test
    @DisplayName("When rebooking a booking that is not canceled, it should throw an exception")
    void rebookBookingThrowsExceptionWhenNotCancelled() {
        UUID bookingId = UUID.randomUUID();

        Booking booking = Booking.builder()
                .id(bookingId)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(false)
                .adults(2)
                .children(1)
                .build();

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.rebookCanceledBooking(bookingId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.BOOKING_NOT_CANCELLED.getMessage());
    }

    @Test
    @DisplayName("When getting a booking it should return the booking")
    void getBookingSuccess() {
        UUID bookingId = UUID.randomUUID();

        Booking expected = Booking.builder()
                .id(bookingId)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(false)
                .adults(2)
                .children(1)
                .build();

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(expected));

        Booking actual = bookingService.getBooking(bookingId);

        verify(bookingDao).get(bookingId);
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("When getting a booking that does not exist, it should throw an exception")
    void getBookingThrowsExceptionWhenNotExist() {
        UUID bookingId = UUID.randomUUID();

        when(bookingDao.get(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBooking(bookingId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.BOOKING_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("When deleting a booking it should delete the booking")
    void deleteBookingSuccess() {
        UUID bookingId = UUID.randomUUID();

        Booking booking = Booking.builder()
                .id(bookingId)
                .dateFrom(LocalDate.of(2026, 10, 21))
                .dateTo(LocalDate.of(2026, 10, 25))
                .message("Some message")
                .canceled(false)
                .adults(2)
                .children(1)
                .build();

        when(bookingDao.get(bookingId)).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(bookingId);

        verify(bookingDao).delete(booking);
    }
}