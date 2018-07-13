package com.financetracker.area.category.service.impl;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.category.exceptions.CategoryExist;
import com.financetracker.area.category.models.CategoryRequestModel;
import com.financetracker.area.category.repository.CategoryRepository;
import com.financetracker.area.category.service.CategoryService;
import com.financetracker.area.transaction_type.TransactionType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createCategory(CategoryRequestModel newCategory, Long userId) {
        if (checkIfCategoryExists(newCategory.getName(), newCategory.getType())) {
            throw new CategoryExist("The name and type already exist");
        }

        Category category = mapper.map(newCategory, Category.class);
        category.setUserId(userId);

        categoryRepository.save(category);
    }

    private boolean checkIfCategoryExists(String name, TransactionType type) {
        return categoryRepository.findByNameAndType(name, type) != null;
    }
}
