package com.hostfully.bookingapp.property.domain;

import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyDao propertyDao;

    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        this.propertyService = new PropertyService(propertyDao);
    }

    @Test
    @DisplayName("Given a property Id when the property exists, should return the property")
    void getPropertySuccess() {
        UUID propertyId = UUID.randomUUID();

        Property expected = Property.builder()
                .id(propertyId)
                .name("Property Name")
                .address("Main street 123")
                .description("Property description")
                .checkInTime("15:00")
                .checkOutTime("11:00")
                .build();

        when(propertyDao.get(propertyId)).thenReturn(Optional.of(expected));

        Property actual = propertyService.getProperty(propertyId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Given an id of a non-existing property, it should throw an exception")
    void getPropertyThrowsExceptionWhenNotExist() {
        UUID propertyId = UUID.randomUUID();

        when(propertyDao.get(propertyId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> propertyService.getProperty(propertyId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.PROPERTY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("When property does not have booking nor block in date range should succeed validation")
    void verifyPropertyAvailabilitySuccess() {
        UUID propertyId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        when(propertyDao.hasBookingInDateRange(propertyId, dateFrom, dateTo, null)).thenReturn(false);
        when(propertyDao.hasBlockInDateRange(propertyId, dateFrom, dateTo, null)).thenReturn(false);

        propertyService.verifyPropertyAvailability(propertyId, dateFrom, dateTo, null, null);
    }

    @Test
    @DisplayName("When a property has a booking in the date range, it should throw an exception")
    void verifyPropertyAvailabilityShouldThrowExceptionWhenHasBooking() {
        UUID propertyId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        when(propertyDao.hasBookingInDateRange(propertyId, dateFrom, dateTo, null)).thenReturn(true);
        when(propertyDao.hasBlockInDateRange(propertyId, dateFrom, dateTo, null)).thenReturn(false);

        assertThatThrownBy(() -> propertyService.verifyPropertyAvailability(propertyId, dateFrom, dateTo, null, null))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.PROPERTY_NOT_AVAILABLE.getMessage());
    }

    @Test
    @DisplayName("When a property has a block in the date range, it should throw an exception")
    void verifyPropertyAvailabilityShouldThrowExceptionWhenHasBlock() {
        UUID propertyId = UUID.randomUUID();
        LocalDate dateFrom = LocalDate.of(2026, 10, 21);
        LocalDate dateTo = LocalDate.of(2026, 10, 25);

        when(propertyDao.hasBookingInDateRange(propertyId, dateFrom, dateTo, null)).thenReturn(false);
        when(propertyDao.hasBlockInDateRange(propertyId, dateFrom, dateTo, null)).thenReturn(true);

        assertThatThrownBy(() -> propertyService.verifyPropertyAvailability(propertyId, dateFrom, dateTo, null, null))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.PROPERTY_NOT_AVAILABLE.getMessage());
    }
}