package com.booking.booking_api.DTORequest;


import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private String description;
}
