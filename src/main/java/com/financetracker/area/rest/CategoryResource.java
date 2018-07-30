package com.financetracker.area.rest;

import com.financetracker.area.category.models.CategoryRequestModel;
import com.financetracker.area.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryResource {

    private final CategoryService categoryService;

    @PostMapping("/{userId}/category")
    public ResponseEntity createCategory(@RequestBody CategoryRequestModel newCategory, @PathVariable Long userId) {
        categoryService.createCategory(newCategory, userId);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
