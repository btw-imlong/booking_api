package com.booking.booking_api.Repositories;

import com.booking.booking_api.Enity.Booking;
import com.booking.booking_api.Enity.BookingStatus;
import com.booking.booking_api.Enity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByServiceAndBookingDateAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
        ServiceEntity service,
        LocalDate bookingDate,
        BookingStatus status,
        LocalTime endTime,
        LocalTime startTime
    );

    List<Booking> findByCustomerEmail(String email);

    List<Booking> findByServiceProviderEmail(String email);

    List<Booking> findByServiceAndBookingDateAndStatus(ServiceEntity service, LocalDate date, BookingStatus status);
}
