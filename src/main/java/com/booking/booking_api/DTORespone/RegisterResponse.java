package com.booking.booking_api.DTORespone;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class RegisterResponse {
    private UUID id;
    private String fullName;
    private String email;
    private List<String> roles;
    private String token;
    private String message;
}
