package com.booking.booking_api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.booking_api.Enity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
