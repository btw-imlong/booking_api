package com.booking.booking_api.Controller;

import com.booking.booking_api.DTORequest.BookingRequest;
import com.booking.booking_api.DTORequest.UpdateBookingStatusRequest;
import com.booking.booking_api.DTORespone.BookingResponse;
import com.booking.booking_api.Service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Only customers can create bookings
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request, Principal principal) {
        return ResponseEntity.ok(bookingService.createBooking(request, principal.getName()));
    }

    // Only providers can approve/reject
    @PutMapping("/update-status/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable UUID bookingId,
            @RequestBody UpdateBookingStatusRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, request, principal.getName()));
    }
    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookingResponse> myBookings(Principal principal) {
        return bookingService.getCustomerBookings(principal.getName());
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public List<BookingResponse> providerBookings(Principal principal) {
        return bookingService.getProviderBookings(principal.getName());
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void cancel(@PathVariable UUID id, Principal principal) {
        bookingService.cancelBooking(id, principal.getName());
    }


}
