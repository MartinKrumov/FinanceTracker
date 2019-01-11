package com.financetracker.area.category.rest;

import com.financetracker.area.category.models.CategoryRequestModel;
import com.financetracker.area.category.models.CategoryResponseModel;
import com.financetracker.area.category.service.CategoryService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryResource {

    private final CategoryService categoryService;

    @ApiOperation(
            value = "Create category",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "Proposal is uploaded successfully"),
            @ApiResponse(code = 400, message = "Validation error. Category with that name already exists"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiImplicitParam(
            name = "newCategory",
            value = "The category in JSON format",
            required = true,
            dataType = "JSON",
            paramType = "CategoryRequestModel"
    )
    @PostMapping("users/{userId}/categories")
    public ResponseEntity createCategory(@Valid @RequestBody CategoryRequestModel newCategory,
                                         @ApiParam(name = "userId", value = "The Id of the user creating category")
                                         @PathVariable Long userId) {
        log.info("Request for creating category has been received with userId = [{}] .", userId);
        categoryService.createCategory(newCategory, userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/categories")
    public ResponseEntity<List<CategoryResponseModel>> getAllCategoriesForUser(@PathVariable Long userId) {
        log.info("Request for getting categories for user has been received with userId = [{}].", userId);
        return ResponseEntity.ok(categoryService.getAllCategoriesForUser(userId));
    }
}
