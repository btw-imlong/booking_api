package com.booking.booking_api.DTORequest;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class BookingRequest {
    private UUID serviceId;
    private LocalDate bookingDate;
    private LocalTime startTime; // "HH:mm" string in JSON
}
