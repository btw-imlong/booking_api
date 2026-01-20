package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.LoginRequest;
import com.booking.booking_api.DTORequest.RegisterRequest;
import com.booking.booking_api.DTORespone.LoginResponse;
import com.booking.booking_api.DTORespone.RegisterResponse;
import com.booking.booking_api.Enity.Role;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.RoleRepository;
import com.booking.booking_api.Repositories.UserRepository;
import com.booking.booking_api.Security.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // Register
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = roleRepository.findByName("PROVIDER")
                .orElseThrow(() -> new RuntimeException("PROVIDER role not found"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus("ACTIVE");
        user.getRoles().add(userRole);

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getStatus(),
                saved.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                "Registration successful"
        );
    }

    // Login
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRoles()
                .stream()
                .findFirst()
                .get()
                .getName());

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                token,
                "Login successful"
        );
    }

    public Object register(String fullName, String email, String password) {
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }
}
