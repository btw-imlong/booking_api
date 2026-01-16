package com.booking.booking_api.Services;


import javax.management.relation.RoleNotFoundException;

import org.apache.coyote.BadRequestException;

import com.booking.booking_api.Dtos.Request.RegisterRequest;
import com.booking.booking_api.Dtos.Response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request) throws BadRequestException, RoleNotFoundException;
}
