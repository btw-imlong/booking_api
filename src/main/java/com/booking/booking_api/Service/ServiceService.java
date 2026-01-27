package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.ServiceRequest;
import com.booking.booking_api.DTORequest.UpdateAvailabilityRequest;
import com.booking.booking_api.DTORespone.ServiceResponse;
import com.booking.booking_api.Enity.Availability;
import com.booking.booking_api.Enity.Booking;
import com.booking.booking_api.Enity.BookingStatus;
import com.booking.booking_api.Enity.ServiceEntity;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.AvailabilityRepository;
import com.booking.booking_api.Repositories.BookingRepository;
import com.booking.booking_api.Repositories.ServiceRepository;
import com.booking.booking_api.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService; 
    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;

    // ------------------ CREATE SERVICE ------------------
    public ServiceResponse createService(ServiceRequest request, String email) {
        User provider = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceEntity service = ServiceEntity.builder()
                .id(UUID.randomUUID()) // MUST generate UUID manually
                .provider(provider)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .isAvailable(true)
                .build();

        ServiceEntity saved = serviceRepository.save(service);
        return mapToResponse(saved);
    }
    public List<LocalTime> getAvailableSlots(UUID serviceId, LocalDate date) {
    	ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        List<Availability> availabilities = availabilityRepository.findByProviderIdAndDate(
                service.getProvider().getId(), date
        );

        List<Booking> bookings = bookingRepository.findByServiceAndBookingDateAndStatus(
                service, date, BookingStatus.APPROVED
        );

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

    // ------------------ GET ALL SERVICES ------------------
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------ GET SERVICE BY ID ------------------
    public ServiceResponse getServiceById(UUID id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return mapToResponse(service);
    }

    // ------------------ GET ONLY IDS ------------------
    public List<UUID> getAllServiceIds() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceEntity::getId)
                .collect(Collectors.toList());
    }

    // ------------------ UPDATE AVAILABILITY ------------------
    public ServiceResponse updateAvailability(UUID id, UpdateAvailabilityRequest request) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setIsAvailable(request.isAvailable());
        ServiceEntity updated = serviceRepository.save(service);

        return mapToResponse(updated);
    }

    // ------------------ HELPER ------------------
    private ServiceResponse mapToResponse(ServiceEntity service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice(),
                service.getDurationMinutes(),
                service.getIsActive(),
                service.getIsAvailable()
        );
    }
}
