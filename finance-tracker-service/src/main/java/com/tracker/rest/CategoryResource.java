package com.tracker.rest;

import com.tracker.domain.Category;
import com.tracker.dto.category.CategoryResponseModel;
import com.tracker.dto.category.CreateCategoryDTO;
import com.tracker.mapper.CategoryMapper;
import com.tracker.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryResource {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @ApiOperation(value = "Create category")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Category is created successfully"),
            @ApiResponse(code = 400, message = "Validation error. Category with that name already exists"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @PostMapping("users/{userId}/categories")
    public ResponseEntity createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO,
                                         @PathVariable Long userId) {
        log.info("Request for creating category has been received with userId = [{}] .", userId);

        Category category = categoryMapper.convertToCategory(createCategoryDTO);

        categoryService.createCategory(category, userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/categories")
    public ResponseEntity<Set<CategoryResponseModel>> getAllCategoriesForUser(@PathVariable Long userId) {
        log.info("Request for getting categories for user has been received with userId = [{}].", userId);

        Set<Category> categories = categoryService.getAllCategoriesForUser(userId);

        return ResponseEntity.ok(categoryMapper.convertToCategoryResponseModels(categories));
    }
}
