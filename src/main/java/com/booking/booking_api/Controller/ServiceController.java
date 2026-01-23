package com.booking.booking_api.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;

import com.booking.booking_api.DTORequest.ServiceRequest;
import com.booking.booking_api.DTORespone.ServiceResponse;
import com.booking.booking_api.Service.ServiceService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping("/create")
    // FIX: Checks for 'ADMIN', 'ROLE_ADMIN', 'PROVIDER', or 'ROLE_PROVIDER'
    // This covers almost all naming conventions to stop the 403 error.
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'PROVIDER', 'ROLE_PROVIDER')")
    public ResponseEntity<ServiceResponse> create(@RequestBody ServiceRequest request, Principal principal) {
        
        // Debug Print: Check your console logs to see what role the user actually has
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DEBUG: User '" + principal.getName() + "' has roles: " + auth.getAuthorities());

        return ResponseEntity.ok(serviceService.createService(request, principal.getName()));
    }
}