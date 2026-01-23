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
import java.util.List;

@RestController
@RequiredArgsConstructor // ✅ Lombok will generate constructor for final fields
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService; // ✅ final field initialized by Lombok

    // ------------------ CREATE SERVICE ------------------
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN','PROVIDER','ROLE_PROVIDER')")
    public ResponseEntity<ServiceResponse> create(@RequestBody ServiceRequest request, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DEBUG: CREATE Service - User '" + principal.getName() + "' roles: " + auth.getAuthorities());
        return ResponseEntity.ok(serviceService.createService(request, principal.getName()));
    }

    // ------------------ GET ALL SERVICES ------------------
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    // Optional `/all` alias
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ServiceResponse>> getAllServicesAlias() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    // ------------------ GET SERVICE BY ID ------------------
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    // ------------------ GET ONLY SERVICE IDS ------------------
    @GetMapping("/ids")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Long>> getServiceIds() {
        return ResponseEntity.ok(serviceService.getAllServiceIds());
    }

    // ------------------ UPDATE AVAILABILITY ------------------
    @PutMapping("/update-availability/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN','PROVIDER','ROLE_PROVIDER')")
    public ResponseEntity<ServiceResponse> updateAvailability(
            @PathVariable Long id,
            @RequestBody UpdateAvailabilityRequest request,
            Principal principal
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DEBUG: Availability update by '" + principal.getName() + "' roles: " + auth.getAuthorities());
        return ResponseEntity.ok(serviceService.updateAvailability(id, request));
    }
}
