package com.bfu.catalogueservice.controller;

import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.controller.payload.Category.CreateCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.UpdateCategoryRequest;
import com.bfu.catalogueservice.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/getAllCategories")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/create-category")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest categoryRequest){
        categoryService.createCategory(categoryRequest);
        log.info("Category has been created");
        return ResponseEntity.ok("Category has been created");
    }

    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryRequest categoryRequest){
        categoryService.updateCategory(categoryRequest);
        log.info("Category has been updated");
        return ResponseEntity.ok("Category has been updated");
    }
    @DeleteMapping("/delete-category")
    public ResponseEntity<?> deleteCategory(@RequestParam String categoryId) {
        categoryService.deleteCategory(categoryId);
        log.info("Category has been deleted");
        return ResponseEntity.noContent().build();
    }
}
