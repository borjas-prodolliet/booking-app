package com.hostfully.bookingapp.property.domain;

import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class PropertyService {

    private final PropertyDao propertyDao;

    public PropertyService(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    public Property getProperty(UUID propertyId) {
        log.info("Getting property {}", propertyId);

        return propertyDao.get(propertyId)
                .orElseThrow(() -> {
                    log.error("Property not found: {}", propertyId);
                    return new AppException(AppExceptionDetail.PROPERTY_NOT_FOUND);
                });
    }

    public void verifyPropertyAvailability(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID bookingId, UUID blockId) {
        log.info("Verifying availability of property {} in date range {} - {}", propertyId, dateFrom, dateTo);

        boolean isPropertyAvailable = propertyAvailableInRange(propertyId, dateFrom, dateTo, bookingId, blockId);

        if (!isPropertyAvailable) {
            log.error("Property not available in requested date range");
            throw new AppException(AppExceptionDetail.PROPERTY_NOT_AVAILABLE);
        }
    }

    public Boolean propertyAvailableInRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID bookingId, UUID blockId) {
        Boolean hasBookingsInDate = propertyDao.hasBookingInDateRange(propertyId, dateFrom, dateTo, bookingId);
        Boolean hasBlocksInDate = propertyDao.hasBlockInDateRange(propertyId, dateFrom, dateTo, blockId);
        return !hasBookingsInDate && !hasBlocksInDate;
    }
}
