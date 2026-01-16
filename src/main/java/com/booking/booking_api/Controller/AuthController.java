package com.booking.booking_api.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.booking.booking_api.DTORequest.LoginRequest;
import com.booking.booking_api.DTORequest.RegisterRequest;
import com.booking.booking_api.DTORespone.LoginResponse;
import com.booking.booking_api.DTORespone.RegisterResponse;
import com.booking.booking_api.Service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                authService.register(
                        request.getFullName(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                authService.login(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }
}
