package com.booking.booking_api.Repository;

import com.booking.booking_api.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(Object username);
    Optional<User> findByEmail(String email);

    // Custom query method
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsernameOrEmail(String username, String email);
    Optional<User> findById(Integer userId);
}
