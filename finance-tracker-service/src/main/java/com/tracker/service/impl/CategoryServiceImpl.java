package com.tracker.service.impl;

import com.tracker.domain.Category;
import com.tracker.dto.category.CategoryRequestModel;
import com.tracker.dto.category.CategoryResponseModel;
import com.tracker.repository.CategoryRepository;
import com.tracker.service.CategoryService;
import com.tracker.service.UserService;
import com.tracker.common.enums.CustomEntity;
import com.tracker.common.exception.EntityAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
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

        var user = userService.findOneOrThrow(userId);

        var category = mapper.map(newCategory, Category.class);
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
        var user = userService.findOneOrThrow(userId);
        Type typeToken = new TypeToken<List<Category>>() {}.getType();
        return mapper.map(user.getCategories(), typeToken);
    }
}
