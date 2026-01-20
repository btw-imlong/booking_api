package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.CreateCategoryRequest;
import com.booking.booking_api.DTORequest.UpdateCategoryRequest;
import com.booking.booking_api.Enity.Category;
import com.booking.booking_api.Enity.Role;
import com.booking.booking_api.Repositories.CategoryRepository;
import com.booking.booking_api.Repositories.UserRepository;


import org.springframework.stereotype.Service;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;



@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private String email;

    public CategoryService(CategoryRepository categoryRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Check role
    private Role getRole(String email) {
        return (Role) userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getRoles();
    }

    // Create Category (Admin only)
    public ResponseEntity<?> createCategory(String email, CreateCategoryRequest request) {
        if (getRole(email) != Role.ADMIN) {
            return ResponseEntity.status(403).body("Only Admin can create category");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);
        return ResponseEntity.ok("Category created successfully");
    }

    // Update Category (Admin only)
    public ResponseEntity<?> updateCategory(Long id, UpdateCategoryRequest request) {
        if (getRole(email) != Role.ADMIN) {
            return ResponseEntity.status(403).body("Only Admin can update category");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);

        return ResponseEntity.ok("Category updated successfully");
    }

    // Delete Category (Admin only)
    public ResponseEntity<?> deleteCategory(Long id) {
        if (getRole(email) != Role.ADMIN) {
            return ResponseEntity.status(403).body("Only Admin can delete category");
        }

        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    public ResponseEntity<?> createCategory(CreateCategoryRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'createCategory'");
    }

}