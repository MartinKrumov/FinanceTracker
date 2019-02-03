package com.tracker.area.category.service;

import com.tracker.area.category.domain.Category;
import com.tracker.area.category.models.CategoryRequestModel;
import com.tracker.area.category.models.CategoryResponseModel;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequestModel newCategory, Long userId);

    Category findOneOrThrow(Long categoryId);

    List<CategoryResponseModel> getAllCategoriesForUser(Long userId);
}
