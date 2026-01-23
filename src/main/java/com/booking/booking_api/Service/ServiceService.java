package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.ServiceRequest;
import com.booking.booking_api.DTORespone.ServiceResponse;
import com.booking.booking_api.Enity.ServiceEntity;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.ServiceRepository;
import com.booking.booking_api.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public ServiceResponse createService(ServiceRequest request, String email) {
        // 1. Get logged-in user
        User provider = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Map request -> entity
        ServiceEntity service = ServiceEntity.builder()
                .provider(provider)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        // 3. Save
        ServiceEntity saved = serviceRepository.save(service);

        // 4. Map entity -> response
        return new ServiceResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getDurationMinutes(),
                saved.getIsActive()
        );
    }
}