package com.booking.booking_api.DTORespone;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class LoginResponse {
    private UUID id;
    private String email;
    private List<String> roles;
    private String token;
    private String message;
}
