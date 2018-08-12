package com.financetracker.area.category.service;

import com.financetracker.area.category.models.CategoryRequestModel;
import com.financetracker.area.category.models.CategoryResponseModel;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequestModel newCategory, Long userId);

    List<CategoryResponseModel> getAllCategoriesForUser(Long userId);
}
