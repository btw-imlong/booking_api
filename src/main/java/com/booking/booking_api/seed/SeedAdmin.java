package com.booking.booking_api.seed;

import com.booking.booking_api.Enity.Role;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.RoleRepository;
import com.booking.booking_api.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SeedAdmin implements CommandLineRunner {

    @Value("${ADMIN_FULLNAME}")
    private String adminFullName;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Check if admin already exists
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            System.out.println("Admin user already exists");
            return;
        }

        // Get roles from DB
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        // Create admin user
        User adminUser = new User();
        adminUser.setFullName(adminFullName);
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setStatus("ACTIVE");

        // Assign roles
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        roles.add(adminRole);
        adminUser.setRoles(roles);

        // Save to DB
        userRepository.save(adminUser);

        System.out.println("Admin user seeded successfully");
    }
}