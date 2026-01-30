package com.booking.booking_api.DTORespone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private String name;
    private String role;
    private String provider;
}