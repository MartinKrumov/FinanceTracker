package com.financetracker.area.category.service.impl;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.category.exceptions.CategoryExist;
import com.financetracker.area.category.models.CategoryRequestModel;
import com.financetracker.area.category.repository.CategoryRepository;
import com.financetracker.area.category.service.CategoryService;
import com.financetracker.area.transaction.domain.enums.TransactionType;
import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.exceptions.UserNotFoundException;
import com.financetracker.area.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createCategory(CategoryRequestModel newCategory, Long userId) {
        if (checkIfCategoryExists(newCategory.getName(), newCategory.getType())) {
            throw new CategoryExist("The name and type already exist");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + userId + "does not exist."));


        Category category = mapper.map(newCategory, Category.class);
        category.setUsers(new HashSet<>(Collections.singletonList(user)));

        categoryRepository.save(category);
    }

    private boolean checkIfCategoryExists(String name, TransactionType type) {
        return categoryRepository.findByNameAndType(name, type) != null;
    }
}
