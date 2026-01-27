package com.booking.booking_api.Repositories;

import com.booking.booking_api.Enity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
    List<Availability> findByProviderIdAndDate(UUID providerId, LocalDate date);
}
