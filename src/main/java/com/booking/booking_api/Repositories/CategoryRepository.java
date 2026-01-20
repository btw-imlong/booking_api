package com.booking.booking_api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.booking.booking_api.Enity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}