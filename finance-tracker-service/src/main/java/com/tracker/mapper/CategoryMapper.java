package com.tracker.mapper;

import com.tracker.domain.Category;
import com.tracker.dto.category.CategoryResponseModel;
import com.tracker.dto.category.CreateCategoryDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category convertToCategory(CreateCategoryDTO requestModel);

    CategoryResponseModel convertToCategoryResponseModel(Category category);

    Set<CategoryResponseModel> convertToCategoryResponseModels(Set<Category> categories);
}
