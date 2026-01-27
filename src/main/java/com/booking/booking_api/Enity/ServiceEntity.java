package com.booking.booking_api.Enity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    // Provider (Owner of service)
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    private String name;
    private String description;
    private BigDecimal price;

    // Duration in minutes
    @Column(name = "duration")
    private Integer durationMinutes;

    // Service active (soft delete / enable-disable)
    @Column(name = "active")
    private Boolean isActive;

    // Service availability (booking open/close)
    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (isActive == null) isActive = true;
        if (isAvailable == null) isAvailable = true;
    }
}
