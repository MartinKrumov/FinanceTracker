package com.tracker.service;

import com.tracker.domain.Category;

import java.util.Set;

public interface CategoryService {
    void createCategory(Category category, Long userId);

    Category findByIdOrThrow(Long categoryId);

    Set<Category> getAllCategoriesForUser(Long userId);
}
