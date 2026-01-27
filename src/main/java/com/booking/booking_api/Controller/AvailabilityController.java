package com.booking.booking_api.Controller;

import com.booking.booking_api.Enity.Availability;
import com.booking.booking_api.Service.AvailabilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // Only providers can create availability
    @PostMapping("/{providerId}")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public Availability createAvailability(
            @PathVariable UUID providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Principal principal
    ) {
        return availabilityService.createAvailability(providerId, date, startTime, endTime);
    }

    // Any authenticated user can view availability
    @GetMapping("/{providerId}/{date}")
    @PreAuthorize("isAuthenticated()")
    public List<Availability> getAvailability(
            @PathVariable UUID providerId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return availabilityService.getAvailability(providerId, date);
    }
}
