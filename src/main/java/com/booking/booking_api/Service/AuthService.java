package com.booking.booking_api.Service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.booking_api.Config.JwtService;
import com.booking.booking_api.DTORespone.LoginResponse;
import com.booking.booking_api.DTORespone.RegisterResponse;
import com.booking.booking_api.Enity.Role;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.RoleRepository;
import com.booking.booking_api.Repositories.UserRepository;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(String fullName, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus("ACTIVE");

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        user.getRoles().add(userRole);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        response.setToken(token);
        response.setMessage("Registration successful");
        return response;
    }

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        response.setToken(token);
        response.setMessage("Login successful");
        return response;
    }
}
