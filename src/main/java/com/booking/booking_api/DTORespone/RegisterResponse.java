package com.booking.booking_api.DTORespone;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RegisterResponse {

    private UUID id;
    private String fullName;
    private String email;
    private String status;
    private Set<String> roles;
    private String message;
}
