package com.tracker.service.impl;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.domain.Category;
import com.tracker.domain.User;
import com.tracker.domain.enums.TransactionType;
import com.tracker.repository.CategoryRepository;
import com.tracker.service.CategoryService;
import com.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void createCategory(Category category, Long userId) {
        checkIfExistsOrThrow(category.getName(), category.getType());

        User user = userService.findByIdOrThrow(userId);

        user.addCategory(category);

        userService.save(user);
    }

    @Override
    public Category findByIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Category> getAllCategoriesForUser(Long userId) {
        Set<Category> categories = userService.findByIdOrThrow(userId).getCategories();
        categories.size();
        return categories;
    }

    /**
     * Checks if there is category with this name or transaction type already exists.
     *
     * @param name username of the category
     * @param type {@link TransactionType}
     */
    private void checkIfExistsOrThrow(String name, TransactionType type) {
        if (categoryRepository.existsByNameAndType(name, type)) {
            throw new EntityAlreadyExistException("Category already exists.");
        }
    }
}
