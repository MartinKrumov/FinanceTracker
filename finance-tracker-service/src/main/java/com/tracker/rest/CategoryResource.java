package com.tracker.rest;

import com.tracker.domain.Category;
import com.tracker.mapper.CategoryMapper;
import com.tracker.rest.dto.category.CreateCategoryDTO;
import com.tracker.rest.dto.category.UserCategoryDTO;
import com.tracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class CategoryResource {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @Operation(summary =  "Create category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category is created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error. Category with that name already exists"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{userId}/categories")
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO,
                                         @PathVariable Long userId) {
        log.info("Request for creating category has been received with userId = [{}] .", userId);

        Category category = categoryMapper.toCategory(createCategoryDTO);

        categoryService.createCategory(category, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}/categories")
    public ResponseEntity<Set<UserCategoryDTO>> getAllCategoriesForUser(@PathVariable Long userId) {
        log.info("Request for getting categories for user has been received with userId = [{}].", userId);

        Set<Category> categories = categoryService.getAllCategoriesForUser(userId);

        return ResponseEntity.ok(categoryMapper.toCategoryResponseModels(categories));
    }
}
