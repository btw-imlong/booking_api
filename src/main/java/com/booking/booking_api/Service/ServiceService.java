package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.ServiceRequest;
import com.booking.booking_api.DTORespone.ServiceResponse;
import com.booking.booking_api.Enity.ServiceEntity;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.ServiceRepository;
import com.booking.booking_api.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    // ------------------ CREATE ------------------
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

    // ------------------ GET ALL ------------------
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(service -> new ServiceResponse(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getPrice(),
                        service.getDurationMinutes(),
                        service.getIsActive()
                ))
                .collect(Collectors.toList());
    }

    // ------------------ GET BY ID ------------------
    public ServiceResponse getServiceById(Long id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice(),
                service.getDurationMinutes(),
                service.getIsActive()
        );
    }

    // ------------------ GET ONLY IDS ------------------
    public List<Long> getAllServiceIds() {
        return serviceRepository.findAll().stream()
                .map(ServiceEntity::getId)
                .collect(Collectors.toList());
    }
}