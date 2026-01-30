package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.CreateCategoryRequest;
import com.booking.booking_api.DTORespone.CategoryResponse;
import com.booking.booking_api.Enity.Category;
import com.booking.booking_api.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Service tells Spring that this class contains the Business Logic.
 * It will be managed by the Spring Container.
 */
@Service
public class CategoryService {

    // Injecting the Repository to perform Database operations (Save, Find, Delete)
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * STEP 1: Helper Method (Mapper)
     * Converts a Database Entity (Category) into a Response Object (CategoryResponse).
     * This keeps your database structure hidden from the user.
     */
    private CategoryResponse toResponse(Category category) {
        CategoryResponse res = new CategoryResponse();
        res.setId(category.getId());
        res.setName(category.getName());
        res.setDescription(category.getDescription());
        return res;
    }

    /**
     * STEP 2: Create Category
     * 1. Receive data from the Request DTO.
     * 2. Turn it into a Category Entity.
     * 3. Save it to the database.
     * 4. Return the saved data as a Response DTO.
     */
    public CategoryResponse create(CreateCategoryRequest request) {
        Category category = new Category(request.getName(), request.getDescription());
        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    /**
     * STEP 3: Update Category
     * 1. Find the existing category by ID.
     * 2. If it doesn't exist, throw an error.
     * 3. Update the fields with the new data.
     * 4. Save the changes and return the response.
     */
    public CategoryResponse update(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);
        return toResponse(updated);
    }

    /**
     * STEP 4: Delete Category
     * Simply tells the repository to remove the record with the matching ID.
     */
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * STEP 5: Get All Categories
     * 1. Fetch the full list of Entities from the database.
     * 2. Create an empty list for the responses.
     * 3. Loop through each Entity, convert it to a Response DTO, and add it to the list.
     */
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> responses = new ArrayList<>();
        
        for (Category category : categories) {
            responses.add(toResponse(category));
        }
        
        return responses;
    }
}