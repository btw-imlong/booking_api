package com.booking.booking_api.DTORequest;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private Boolean isActive;
}