package com.booking.booking_api.Services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.booking_api.Dtos.Request.RegisterRequest;
import com.booking.booking_api.Dtos.Response.RegisterResponse;
import com.booking.booking_api.Entity.Role;
import com.booking.booking_api.Entity.User;
import com.booking.booking_api.Entity.UserRole;
import com.booking.booking_api.Exception.ConflictException;
import com.booking.booking_api.Repository.RoleRepository;
import com.booking.booking_api.Repository.UserRepository;
import com.booking.booking_api.Repository.UserRoleRepository;
import com.booking.booking_api.Services.AuthService;
import com.booking.booking_api.Utils.Validation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest request) throws BadRequestException, RoleNotFoundException {

        // Validate request
        Validation.validateRegister(request);

        // Check if username/email already exists
        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new ConflictException("Username or email already exists");
        }

        // Create and save new user
        User user = createUser(request);
        userRepository.save(user);

        // Assign roles
        assignRole(user, "CUSTOMER");
        if (request.isServiceProvider()) {
            assignRole(user, "SERVICE_PROVIDER");
        }

        // Extract role names for response
        List<String> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .map(Role::getName)
                .collect(Collectors.toList());

      

         return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                roles
        );
    }

    // Helper: create user entity from request
    private User createUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new ArrayList<>());
        return user;
    }

    // Helper: assign role to user
    private void assignRole(User user, String roleName) throws RoleNotFoundException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName + " role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        user.getRoles().add(userRole);
        userRoleRepository.save(userRole);
    }
}
