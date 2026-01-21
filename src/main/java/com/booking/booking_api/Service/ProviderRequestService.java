package com.booking.booking_api.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.booking.booking_api.DTORequest.RequestStatus;
import com.booking.booking_api.Enity.ProviderRequest;
import com.booking.booking_api.Enity.Role;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.ProviderRequestRepository;
import com.booking.booking_api.Repositories.RoleRepository;
import com.booking.booking_api.Repositories.UserRepository;


import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderRequestService {

    private final ProviderRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // CUSTOMER sends request
    public ProviderRequest createRequest(User customer) {
        if (customer.getRoles().stream().anyMatch(r -> r.getName().equals("PROVIDER"))) {
            throw new RuntimeException("You are already a provider");
        }

        boolean pendingExists = requestRepository.findByUser(customer).stream()
                .anyMatch(r -> r.getStatus() == RequestStatus.PENDING);

        if (pendingExists) {
            throw new RuntimeException("You already have a pending request");
        }

        ProviderRequest request = ProviderRequest.builder()
                .user(customer)
                .status(RequestStatus.PENDING)
                .build();

        return requestRepository.save(request);
    }

    // ADMIN views all requests
    public List<ProviderRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    // ADMIN approves request
    public ProviderRequest approveRequest(UUID id, User admin) {
        checkAdmin(admin);

        ProviderRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        // Add PROVIDER role to user
        User user = request.getUser();
        Role providerRole = roleRepository.findByName("PROVIDER")
                .orElseThrow(() -> new RuntimeException("Role PROVIDER not found"));

        user.setRoles(Set.of(providerRole)); // ✅ only PROVIDER role
        userRepository.save(user);

        return request;
    }

    // ADMIN rejects request
    public ProviderRequest rejectRequest(UUID id, User admin) {
        checkAdmin(admin);

        ProviderRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    // Only ADMIN can approve/reject
    private void checkAdmin(User user) {
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin) throw new AccessDeniedException("Only ADMIN can approve/reject requests");
    }
}
