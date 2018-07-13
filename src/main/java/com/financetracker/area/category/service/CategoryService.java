package com.financetracker.area.category.service;

import com.financetracker.area.category.models.CategoryRequestModel;

public interface CategoryService {
    void createCategory(CategoryRequestModel newCategory, Long userId);
}
