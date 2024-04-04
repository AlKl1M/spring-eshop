package com.bfu.catalogueservice.service.category;

import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.controller.payload.Category.CreateCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.DeleteCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.UpdateCategoryRequest;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.exception.CategoryNotFoundException;
import com.bfu.catalogueservice.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    @Override
    public void createCategory(CreateCategoryRequest categoryRequest) {
        Category category = Category.builder()
                .categoryId(UUID.randomUUID().toString().substring(0,15))
                .name(categoryRequest.name())
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(DeleteCategoryRequest categoryRequest) {
        Category category = categoryRepository.findByCategoryId(categoryRequest.categoryId());
        if (category != null){
            categoryRepository.delete(category);
        }
        else {
            throw new CategoryNotFoundException(categoryRequest.categoryId());
        }

    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> categories = new ArrayList<>();
        for (Category category: categoryRepository.findAll()){
            categories.add(CategoryResponse.from(category));
        }
        return categories;
    }

    @Override
    public void updateCategory(UpdateCategoryRequest categoryRequest) {
        Category category = categoryRepository.findByCategoryId(categoryRequest.categoryId());
        if (category != null){
            category.setName(categoryRequest.newName());
            categoryRepository.save(category);
        }
        else {
            throw new CategoryNotFoundException(categoryRequest.categoryId());
        }
    }
}
