package com.tracker.mapper;

import com.tracker.domain.Category;
import com.tracker.rest.dto.category.CreateCategoryDTO;
import com.tracker.rest.dto.category.UserCategoryDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CreateCategoryDTO requestModel);

    UserCategoryDTO toCategoryResponseModel(Category category);

    Set<UserCategoryDTO> toCategoryResponseModels(Set<Category> categories);
}
