package com.booking.booking_api.DTORequest;


import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    private String description;
}