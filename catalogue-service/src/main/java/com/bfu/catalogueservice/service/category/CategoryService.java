package com.bfu.catalogueservice.service.category;


import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.controller.payload.Category.CreateCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    void createCategory(CreateCategoryRequest categoryRequest);

    void deleteCategory(String categoryId);

    List<CategoryResponse> getAllCategories();

    void updateCategory(UpdateCategoryRequest categoryRequest);
}
