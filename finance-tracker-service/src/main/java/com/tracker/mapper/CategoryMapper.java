package com.tracker.mapper;

import com.tracker.domain.Category;
import com.tracker.rest.dto.category.UserCategoryDTO;
import com.tracker.rest.dto.category.CreateCategoryDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category convertToCategory(CreateCategoryDTO requestModel);

    UserCategoryDTO convertToCategoryResponseModel(Category category);

    Set<UserCategoryDTO> convertToCategoryResponseModels(Set<Category> categories);
}
