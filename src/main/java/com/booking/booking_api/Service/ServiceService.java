package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.ServiceRequest;
import com.booking.booking_api.DTORequest.UpdateAvailabilityRequest;
import com.booking.booking_api.DTORespone.ServiceResponse;
import com.booking.booking_api.Enity.ServiceEntity;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.ServiceRepository;
import com.booking.booking_api.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    // ================= CREATE SERVICE =================
    public ServiceResponse createService(ServiceRequest request, String email) {

        User provider = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceEntity service = ServiceEntity.builder()
                .provider(provider)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .isAvailable(true) // default available
                .build();

        ServiceEntity saved = serviceRepository.save(service);

        return new ServiceResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getDurationMinutes(),
                saved.getIsActive()
        );
    }

    // ================= UPDATE AVAILABILITY =================
    public ResponseEntity<?> updateAvailability(
            Long serviceId,
            UpdateAvailabilityRequest request
    ) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setIsAvailable(request.isAvailable());

        serviceRepository.save(service);

        return ResponseEntity.ok("Service availability updated successfully");
    }
}
