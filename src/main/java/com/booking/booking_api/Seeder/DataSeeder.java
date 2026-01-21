package com.booking.booking_api.Seeder;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.booking.booking_api.Enity.Category;
import com.booking.booking_api.Repositories.CategoryRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataSeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Prevent duplicate seeding
        if (categoryRepository.count() > 0) {
            return;
        }

        List<Category> categories = List.of(
                new Category(null, "Food"),
                new Category(null, "Hotel"),
                new Category(null, "Transport"),
                new Category(null, "Entertainment"),
                new Category(null, "Travel")
        );

        categoryRepository.saveAll(categories);
    }
}
