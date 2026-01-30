package com.booking.booking_api.DTORequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialAuthRequest {
    private String token; // Google ID Token OR Facebook Access Token
}