package com.booking.booking_api.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.booking.booking_api.Enity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}


// this repository is easy use data jpa can do get all , get by id, delete, update, create without code anything