package com.bookingapp.property.infrastructure;

import com.bookingapp.block.domain.Block;
import com.bookingapp.booking.domain.Booking;
import com.bookingapp.property.domain.Property;
import com.bookingapp.user.domain.LegalIdType;
import com.bookingapp.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PropertyRepositoryTest {

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    TestEntityManager entityManager;

    private static final LocalDate DATE_FROM = LocalDate.of(2026, 10, 21);
    private static final LocalDate DATE_TO = LocalDate.of(2026, 10, 25);

    @ParameterizedTest
    @MethodSource("provideOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a booking in that date range and overlapping date ranges, should return true")
    void hasBookingInDateRangeReturnsTrueWhenBookingExists(LocalDate overlapFrom, LocalDate overlapTo) {
        Property savedProperty = buildAndPersistProperty();
        User savedUser = buildAndPersistUser();

        Booking booking = Booking.builder()
                .property(savedProperty)
                .mainGuest(savedUser)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .canceled(false)
                .message("Some message")
                .adults(2)
                .children(1)
                .build();

        entityManager.persistAndFlush(booking);

        boolean hasBookingInDateRange = propertyRepository.hasBookingInDateRange(savedProperty.getId(), overlapFrom, overlapTo, null);
        assertTrue(hasBookingInDateRange);
    }

    @ParameterizedTest
    @MethodSource("provideOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a booking in that date range and overlapping date ranges, " +
            "but providing the same booking id, should ignore it and return false")
    void hasBookingInDateRangeReturnsFalseWhenIsSameBooking(LocalDate overlapFrom, LocalDate overlapTo) {
        Property savedProperty = buildAndPersistProperty();
        User savedUser = buildAndPersistUser();

        Booking booking = Booking.builder()
                .property(savedProperty)
                .mainGuest(savedUser)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .canceled(false)
                .message("Some message")
                .adults(2)
                .children(1)
                .build();

        Booking savedBooking = entityManager.persistAndFlush(booking);

        boolean hasBookingInDateRange = propertyRepository.hasBookingInDateRange(savedProperty.getId(), overlapFrom, overlapTo, savedBooking.getId());
        assertFalse(hasBookingInDateRange);
    }

    @ParameterizedTest
    @MethodSource("provideOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a cancelled booking in that date range and overlapping date ranges, " +
            "should return false")
    void hasBookingInDateRangeReturnsFalseWhenHasCancelledBooking(LocalDate overlapFrom, LocalDate overlapTo) {
        Property savedProperty = buildAndPersistProperty();
        User savedUser = buildAndPersistUser();

        Booking booking = Booking.builder()
                .property(savedProperty)
                .mainGuest(savedUser)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .canceled(true)
                .message("Some message")
                .adults(2)
                .children(1)
                .build();

        entityManager.persistAndFlush(booking);

        boolean hasBookingInDateRange = propertyRepository.hasBookingInDateRange(savedProperty.getId(), overlapFrom, overlapTo, null);
        assertFalse(hasBookingInDateRange);
    }

    @ParameterizedTest
    @MethodSource("provideNonOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a booking in that date range and non-overlapping date ranges, should return false")
    void hasBookingInDateRangeReturnsFalseWhenNotOverlappingDates(LocalDate nonOverlapFrom, LocalDate nonOverlapTo) {
        Property savedProperty = buildAndPersistProperty();
        User savedUser = buildAndPersistUser();

        Booking booking = Booking.builder()
                .property(savedProperty)
                .mainGuest(savedUser)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .canceled(false)
                .message("Some message")
                .adults(2)
                .children(1)
                .build();

        entityManager.persistAndFlush(booking);

        boolean hasBookingInDateRange = propertyRepository.hasBookingInDateRange(savedProperty.getId(), nonOverlapFrom, nonOverlapTo, null);
        assertFalse(hasBookingInDateRange);
    }

    @ParameterizedTest
    @MethodSource("provideOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a block in that date range and overlapping date ranges, should return true")
    void hasBlockInDateRangeReturnsTrueWhenBlockExists(LocalDate overlapFrom, LocalDate overlapTo) {
        Property savedProperty = buildAndPersistProperty();

        Block block = Block.builder()
                .property(savedProperty)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .build();

        entityManager.persistAndFlush(block);

        boolean hasBlockInDateRange = propertyRepository.hasBlockInDateRange(savedProperty.getId(), overlapFrom, overlapTo, null);
        assertTrue(hasBlockInDateRange);
    }

    @ParameterizedTest
    @MethodSource("provideOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a block in that date range and overlapping date ranges, " +
            "but providing the same block id, should ignore it and return false")
    void hasBlockInDateRangeReturnsFalseWhenIsSameBlock(LocalDate overlapFrom, LocalDate overlapTo) {
        Property savedProperty = buildAndPersistProperty();

        Block block = Block.builder()
                .property(savedProperty)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .build();

        Block savedBlock = entityManager.persistAndFlush(block);

        boolean hasBlockInDateRange = propertyRepository.hasBlockInDateRange(savedProperty.getId(), overlapFrom, overlapTo, savedBlock.getId());
        assertFalse(hasBlockInDateRange);
    }

    @ParameterizedTest
    @MethodSource("provideNonOverlappingDateRanges")
    @DisplayName("Given a property and a date range, a block in that date range and non-overlapping date ranges, should return false")
    void hasBlockInDateRangeReturnsFalseWhenNotOverlappingDates(LocalDate nonOverlapFrom, LocalDate nonOverlapTo) {
        Property savedProperty = buildAndPersistProperty();

        Block block = Block.builder()
                .property(savedProperty)
                .dateFrom(DATE_FROM)
                .dateTo(DATE_TO)
                .build();

        entityManager.persistAndFlush(block);

        boolean hasBlockInDateRange = propertyRepository.hasBlockInDateRange(savedProperty.getId(), nonOverlapFrom, nonOverlapTo, null);
        assertFalse(hasBlockInDateRange);
    }

    private static Stream<Arguments> provideOverlappingDateRanges() {
        return Stream.of(
                Arguments.of(LocalDate.of(2026, 10, 21), LocalDate.of(2026, 10, 25)),
                Arguments.of(LocalDate.of(2026, 10, 19), LocalDate.of(2026, 10, 23)),
                Arguments.of(LocalDate.of(2026, 10, 23), LocalDate.of(2026, 10, 28)),
                Arguments.of(LocalDate.of(2026, 10, 22), LocalDate.of(2026, 10, 24))
        );
    }

    private static Stream<Arguments> provideNonOverlappingDateRanges() {
        return Stream.of(
                Arguments.of(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 15)),
                Arguments.of(LocalDate.of(2026, 10, 28), LocalDate.of(2026, 10, 30))
        );
    }

    private Property buildAndPersistProperty() {
        Property property = Property.builder()
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .nightlyPrice(100.0)
                .build();

        return entityManager.persistAndFlush(property);
    }

    private User buildAndPersistUser() {
        User user = User.builder()
                .email("email@example.com")
                .firstName("Jon")
                .lastName("Snow")
                .legalId("12341234")
                .legalIdType(LegalIdType.NATIONAL_ID)
                .build();

        return entityManager.persistAndFlush(user);
    }
}