package com.booking.booking_api.Enity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 👤 CUSTOMER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // 🛎️ SERVICE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

   
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }

}
