package com.booking.booking_api.DTORespone;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {

    private UUID id;
    private String email;
    private Set<String> roles;
    private String message;
    private String token;

}
