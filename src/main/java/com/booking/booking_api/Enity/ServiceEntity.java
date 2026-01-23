package com.booking.booking_api.Enity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider; 

    private String name;
    private String description;
    private BigDecimal price;
    
    // ----------- CHANGE THIS LINE -----------
    @Column(name = "duration") // Changed from "duration_minutes" to "duration"
    private Integer durationMinutes;
    // ----------------------------------------
    
    @Column(name = "active") 
    private Boolean isActive;
    
    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (isActive == null) isActive = true;
    }
}