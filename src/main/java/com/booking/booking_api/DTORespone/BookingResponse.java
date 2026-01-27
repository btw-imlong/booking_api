package com.booking.booking_api.DTORespone;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class BookingResponse {
    private UUID id;
    private UUID customerId;
    private UUID serviceId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
