package com.hostfully.bookingapp.booking.domain;

import com.hostfully.bookingapp.property.domain.Property;
import com.hostfully.bookingapp.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "property_id",
            referencedColumnName = "id"
    )
    private Property property;

    @Column
    private LocalDate dateFrom;

    @Column
    private LocalDate dateTo;

    @ManyToOne
    @JoinColumn(
            name = "main_guest_id",
            referencedColumnName = "id"
    )
    private User mainGuest;

    @Column
    private String message;

    @Column
    private Boolean canceled;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GuestsDetail> guestsDetails;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public User getMainGuest() {
        return mainGuest;
    }

    public void setMainGuest(User mainGuest) {
        this.mainGuest = mainGuest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public List<GuestsDetail> getGuestsDetails() {
        return guestsDetails;
    }

    public void setGuestsDetails(List<GuestsDetail> guestsDetails) {
        this.guestsDetails = guestsDetails;
    }
}
