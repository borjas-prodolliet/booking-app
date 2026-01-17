package com.hostfully.bookingapp.property.infrastructure;

import com.hostfully.bookingapp.property.domain.Property;
import com.hostfully.bookingapp.property.domain.PropertyDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class PropertyJpaDataAccessService implements PropertyDao {

    private final PropertyRepository propertyRepository;

    public PropertyJpaDataAccessService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Optional<Property> get(UUID propertyId) {
        return propertyRepository.findById(propertyId);
    }

    @Override
    public Boolean hasBookingInDateRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID bookingId) {
        return propertyRepository.hasBookingInDateRange(propertyId, dateFrom, dateTo, bookingId);
    }

    @Override
    public Boolean hasBlockInDateRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID blockId) {
        return propertyRepository.hasBlockInDateRange(propertyId, dateFrom, dateTo, blockId);
    }

}
