package com.tracker.service;

import com.tracker.domain.Category;
import com.tracker.dto.category.CategoryRequestModel;
import com.tracker.dto.category.CategoryResponseModel;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequestModel newCategory, Long userId);

    Category findByIdOrThrow(Long categoryId);

    List<CategoryResponseModel> getAllCategoriesForUser(Long userId);
}
