package com.tracker.service.impl;

import com.tracker.common.enums.CustomEntity;
import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.domain.Category;
import com.tracker.domain.User;
import com.tracker.dto.category.CategoryRequestModel;
import com.tracker.dto.category.CategoryResponseModel;
import com.tracker.repository.CategoryRepository;
import com.tracker.service.CategoryService;
import com.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createCategory(CategoryRequestModel newCategory, Long userId) {
        categoryRepository.findByNameAndType(newCategory.getName(), newCategory.getType())
                .orElseThrow(() -> new EntityAlreadyExistException(CustomEntity.CATEGORY));

        User user = userService.findOneOrThrow(userId);

        Category category = mapper.map(newCategory, Category.class);
        category.setUserId(userId);
        user.addCategory(category);

        userService.save(user);
    }

    @Override
    public Category findOneOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseModel> getAllCategoriesForUser(Long userId) {
        User user = userService.findOneOrThrow(userId);
        var typeToken = new TypeToken<List<Category>>() {}.getType();
        return mapper.map(user.getCategories(), typeToken);
    }
}
