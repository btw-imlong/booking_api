package com.booking.booking_api.Controller;

import com.booking.booking_api.DTORequest.ServiceRequest;
import com.booking.booking_api.DTORequest.UpdateAvailabilityRequest;
import com.booking.booking_api.DTORespone.ServiceResponse;
import com.booking.booking_api.Service.ServiceService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    // ================= CREATE SERVICE =================
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'PROVIDER', 'ROLE_PROVIDER')")
    public ResponseEntity<ServiceResponse> create(
            @RequestBody ServiceRequest request,
            Principal principal
    ) {
        // Debug roles
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DEBUG: User '" + principal.getName() + "' has roles: " + auth.getAuthorities());

        return ResponseEntity.ok(
                serviceService.createService(request, principal.getName())
        );
    }

    // ================= UPDATE AVAILABILITY =================
    @PutMapping("/update-availability/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'PROVIDER', 'ROLE_PROVIDER')")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestBody UpdateAvailabilityRequest request,
            Principal principal
    ) {
        // Optional: debug who updates availability
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DEBUG: Availability update by '" 
                + principal.getName() + "' roles: " + auth.getAuthorities());

        return serviceService.updateAvailability(id, request);
    }
}
