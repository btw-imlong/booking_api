package com.booking.booking_api.Service;

import com.booking.booking_api.DTORequest.CreateCategoryRequest;
import com.booking.booking_api.DTORespone.CategoryResponse;
import com.booking.booking_api.Enity.Category;
import com.booking.booking_api.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryResponse toResponse(Category category) {
        CategoryResponse res = new CategoryResponse();
        res.setId(category.getId());
        res.setName(category.getName());
        res.setDescription(category.getDescription());
        return res;
    }


    public CategoryResponse create(CreateCategoryRequest request) {
        Category category = new Category(request.getName(), request.getDescription());
        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    public CategoryResponse update(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);
        return toResponse(updated);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
