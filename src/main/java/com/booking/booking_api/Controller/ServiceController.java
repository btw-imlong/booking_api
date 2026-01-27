	package com.booking.booking_api.Controller;
	
	import com.booking.booking_api.DTORequest.ServiceRequest;
	import com.booking.booking_api.DTORequest.UpdateAvailabilityRequest;
	import com.booking.booking_api.DTORespone.ServiceResponse;
	import com.booking.booking_api.Service.ServiceService;
	import lombok.RequiredArgsConstructor;
	import org.springframework.http.ResponseEntity;
	import org.springframework.security.access.prepost.PreAuthorize;
	
	import org.springframework.web.bind.annotation.*;
	
	import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
	
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    // Only ROLE_PROVIDER or ROLE_ADMIN can create
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest request, Principal principal) {
        return ResponseEntity.ok(serviceService.createService(request, principal.getName()));
    }

    // Anyone authenticated can read services
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }
    @GetMapping("/{id}/available-slots")
    @PreAuthorize("isAuthenticated()")
    public List<LocalTime> availableSlots(@PathVariable UUID id,
                                          @RequestParam LocalDate date) {
        return serviceService.getAvailableSlots(id, date);
    }
}
