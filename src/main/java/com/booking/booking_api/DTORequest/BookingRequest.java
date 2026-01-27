package com.booking.booking_api.DTORequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class BookingRequest {

    private UUID serviceId;

    private LocalDate bookingDate;

    @JsonFormat(pattern = "HH:mm")
    @Schema(
        type = "string",
        example = "09:00",
        description = "Start time in HH:mm format"
    )
    private LocalTime startTime;
}
