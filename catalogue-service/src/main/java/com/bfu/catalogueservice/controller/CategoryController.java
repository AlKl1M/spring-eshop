package com.bfu.catalogueservice.controller;

import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.controller.payload.Category.CreateCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.DeleteCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.UpdateCategoryRequest;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/getAllCategories")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/create-category")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest categoryRequest){
        categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok("Brand has been created");
    }
    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryRequest categoryRequest){
        categoryService.updateCategory(categoryRequest);
        return ResponseEntity.ok("Brand has been updated");
    }
    @DeleteMapping("/delete-category")
    public ResponseEntity<?> deleteCategory(@RequestBody DeleteCategoryRequest categoryRequest) {
        categoryService.deleteCategory(categoryRequest);
        return ResponseEntity.noContent().build();
    }
}
