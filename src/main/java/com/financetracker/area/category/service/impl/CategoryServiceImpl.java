package com.financetracker.area.category.service.impl;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.category.models.CategoryRequestModel;
import com.financetracker.area.category.models.CategoryResponseModel;
import com.financetracker.area.category.repository.CategoryRepository;
import com.financetracker.area.category.service.CategoryService;
import com.financetracker.area.transaction.enums.TransactionType;
import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.repositories.UserRepository;
import com.financetracker.enums.CustomEntity;
import com.financetracker.exception.EntityAlreadyExistException;
import com.financetracker.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
            throw new EntityAlreadyExistException(CustomEntity.CATEGORY);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(CustomEntity.USER));


        Category category = mapper.map(newCategory, Category.class);
        category.setUsers(new HashSet<>(Collections.singletonList(user)));

        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponseModel> getAllCategoriesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(CustomEntity.USER));

        List<Category> categories = categoryRepository.findAllByUsers(user);

        Type typeToken = new TypeToken<List<Category>>() {}.getType();

        return mapper.map(categories, typeToken);
    }

    private boolean checkIfCategoryExists(String name, TransactionType type) {
        return categoryRepository.findByNameAndType(name, type) != null;
    }
}
