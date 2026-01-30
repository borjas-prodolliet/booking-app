package com.bookingapp.property.infrastructure;

import com.bookingapp.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query("""
                    SELECT COUNT(p) > 0 FROM Property p JOIN Booking bo ON p = bo.property
                    WHERE p.id = :propertyId
                    AND (bo.id != :bookingId OR :bookingId IS NULL)
                    AND bo.canceled = false
                    AND bo.dateFrom <= :dateTo
                    AND bo.dateTo >= :dateFrom
            """)
    boolean hasBookingInDateRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID bookingId);

    @Query("""
                    SELECT COUNT(p) > 0 FROM Property p JOIN Block bl ON p = bl.property
                    WHERE p.id = :propertyId
                    AND (bl.id != :blockId OR :blockId IS NULL)
                    AND bl.dateFrom <= :dateTo
                    AND bl.dateTo >= :dateFrom
            """)
    boolean hasBlockInDateRange(UUID propertyId, LocalDate dateFrom, LocalDate dateTo, UUID blockId);
}
