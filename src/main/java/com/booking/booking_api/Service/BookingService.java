package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.BookingRequest;
import com.booking.booking_api.DTORequest.UpdateBookingStatusRequest;
import com.booking.booking_api.DTORespone.BookingResponse;
import com.booking.booking_api.Enity.*;
import com.booking.booking_api.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;

    public BookingResponse createBooking(BookingRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        ServiceEntity service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!service.getIsActive()) throw new RuntimeException("Service is not active");

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = startTime.plusMinutes(service.getDurationMinutes());

        // ✅ Check provider availability
        List<Availability> availabilities = availabilityRepository
                .findByProviderIdAndDate(service.getProvider().getId(), request.getBookingDate());

        boolean slotAvailable = availabilities.stream().anyMatch(a ->
                !startTime.isBefore(a.getStartTime()) && !endTime.isAfter(a.getEndTime())
        );
        if (!slotAvailable) throw new RuntimeException("Provider is not available at this time");

        // ✅ Check overlapping bookings (ONLY APPROVED block)
        List<Booking> overlapping = bookingRepository
                .findByServiceAndBookingDateAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        service,
                        request.getBookingDate(),
                        BookingStatus.APPROVED,
                        endTime,
                        startTime
                );
        if (!overlapping.isEmpty()) throw new RuntimeException("Time slot already booked");

        // ✅ Save booking
        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .service(service)
                .bookingDate(request.getBookingDate())
                .startTime(startTime)
                .endTime(endTime)
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        return mapToResponse(saved);
    }

    public BookingResponse updateBookingStatus(UUID bookingId, UpdateBookingStatusRequest request, String providerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getService().getProvider().getEmail().equals(providerEmail))
            throw new RuntimeException("Forbidden");

        if (booking.getStatus() != BookingStatus.PENDING)
            throw new RuntimeException("Booking already processed");

        booking.setStatus(request.getStatus());
        Booking updated = bookingRepository.save(booking);

        return mapToResponse(updated);
    }

    public List<BookingResponse> getCustomerBookings(String email) {
        return bookingRepository.findByCustomerEmail(email).stream().map(this::mapToResponse).toList();
    }

    public List<BookingResponse> getProviderBookings(String email) {
        return bookingRepository.findByServiceProviderEmail(email).stream().map(this::mapToResponse).toList();
    }

    public void cancelBooking(UUID bookingId, String customerEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getCustomer().getEmail().equals(customerEmail))
            throw new RuntimeException("Forbidden");

        if (booking.getStatus() == BookingStatus.APPROVED)
            throw new RuntimeException("Cannot cancel approved booking");

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<LocalTime> getAvailableSlots(ServiceEntity service, LocalDate date) {
        List<Availability> availabilities = availabilityRepository.findByProviderIdAndDate(service.getProvider().getId(), date);
        List<Booking> bookings = bookingRepository.findByServiceAndBookingDateAndStatus(service, date, BookingStatus.APPROVED);

        List<LocalTime> slots = new ArrayList<>();
        for (Availability a : availabilities) {
        	LocalTime t = a.getStartTime();
        	while (!t.plusMinutes(service.getDurationMinutes()).isAfter(a.getEndTime())) {
        	    LocalTime startSlot = t; // ✅ final copy for lambda
        	    LocalTime endSlot = t.plusMinutes(service.getDurationMinutes());

        	    boolean clash = bookings.stream().anyMatch(b ->
        	        !endSlot.isBefore(b.getStartTime()) &&
        	        !startSlot.isAfter(b.getEndTime())
        	    );

        	    if (!clash) slots.add(startSlot);

        	    t = t.plusMinutes(service.getDurationMinutes());
        	}

        }
        return slots;
    }

    private BookingResponse mapToResponse(Booking b) {
        BookingResponse r = new BookingResponse();
        r.setId(b.getId());
        r.setCustomerId(b.getCustomer().getId());
        r.setServiceId(b.getService().getId());
        r.setBookingDate(b.getBookingDate());
        r.setStartTime(b.getStartTime());
        r.setEndTime(b.getEndTime());
        r.setStatus(b.getStatus().name());
        return r;
    }
}
