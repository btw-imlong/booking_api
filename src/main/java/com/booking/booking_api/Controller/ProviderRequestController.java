package com.booking.booking_api.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.booking.booking_api.Enity.ProviderRequest;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.UserRepository;
import com.booking.booking_api.Service.ProviderRequestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/provider-requests")
@RequiredArgsConstructor
public class ProviderRequestController {

    private final ProviderRequestService requestService;
    private final UserRepository userRepository;

    // CUSTOMER sends request
    @PostMapping
    public ProviderRequest sendRequest(Authentication authentication) {
        // Get email/username from JWT
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return requestService.createRequest(user);
    }

    // ADMIN views all requests
    @GetMapping
    public List<ProviderRequest> getAllRequests(Authentication authentication) {
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return requestService.getAllRequests();
    }

    // ADMIN approves request
    @PutMapping("/{id}/approve")
    public ProviderRequest approve(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return requestService.approveRequest(id, admin);
    }

    // ADMIN rejects request
    @PutMapping("/{id}/reject")
    public ProviderRequest reject(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return requestService.rejectRequest(id, admin);
    }
}
