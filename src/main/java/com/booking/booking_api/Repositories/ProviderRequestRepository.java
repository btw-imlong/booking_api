package com.booking.booking_api.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.booking_api.Enity.ProviderRequest;
import com.booking.booking_api.Enity.User;

import java.util.List;
import java.util.UUID;

public interface ProviderRequestRepository extends JpaRepository<ProviderRequest, UUID> {
    List<ProviderRequest> findByUser(User user);
}
