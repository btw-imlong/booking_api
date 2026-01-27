package com.booking.booking_api.DTORequest;

import com.booking.booking_api.Enity.BookingStatus;

import lombok.Data;

@Data
public class UpdateBookingStatusRequest {
	private BookingStatus status;
 // "APPROVED" or "REJECTED"
}
