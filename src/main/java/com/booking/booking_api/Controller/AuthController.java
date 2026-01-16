package com.booking.booking_api.Controller;

import javax.management.relation.RoleNotFoundException;

import org.apache.coyote.BadRequestException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.booking_api.Dtos.Request.RegisterRequest;
import com.booking.booking_api.Dtos.Response.RegisterResponse;
import com.booking.booking_api.Services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
       private final AuthService authService;

     @PostMapping("/register")
    public ResponseEntity<@Nullable Object> register(
            @RequestBody RegisterRequest request) throws RoleNotFoundException, BadRequestException {

        RegisterResponse res = authService.register(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.success(201, "User registered successfully", res));
    }
}