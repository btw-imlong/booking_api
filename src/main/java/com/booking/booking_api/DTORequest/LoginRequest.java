package com.booking.booking_api.DTORequest;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
