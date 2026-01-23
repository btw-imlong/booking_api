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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

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
        List<ServiceResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // Optional `/all` alias
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ServiceResponse>> getAllServicesAlias() {
        List<ServiceResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // ------------------ GET SERVICE BY ID ------------------
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    // ------------------ GET ONLY SERVICE IDS ------------------
    @GetMapping("/ids")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Long>> getServiceIds() {
        List<Long> ids = serviceService.getAllServiceIds();
        return ResponseEntity.ok(ids);
    }
}