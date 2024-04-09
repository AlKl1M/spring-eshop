package com.bfu.catalogueservice.service.category;

import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.controller.payload.Category.CreateCategoryRequest;
import com.bfu.catalogueservice.controller.payload.Category.UpdateCategoryRequest;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.exception.CategoryNotFoundException;
import com.bfu.catalogueservice.repository.CategoryRepository;
import com.bfu.catalogueservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Override
    public void createCategory(CreateCategoryRequest categoryRequest) {
        log.info("Start creating category with name {}", categoryRequest.name());
        Category category = Category.builder()
                .categoryId(UUID.randomUUID().toString().substring(0,15))
                .name(categoryRequest.name())
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String categoryId) {
        log.info("Start deleting brand with categoryId {}", categoryId);
        Category category = categoryRepository.findByCategoryId(categoryId);
        if (category != null){
            productRepository.deleteAll(productRepository.findAllByCategory(category));
            categoryRepository.delete(category);
        }
        else {
            log.error("Category not found with categoryId {}", categoryId);
            throw new CategoryNotFoundException(categoryId);
        }
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Getting all categories");
        List<CategoryResponse> categories = new ArrayList<>();
        for (Category category: categoryRepository.findAll()){
            categories.add(CategoryResponse.from(category));
        }
        return categories;
    }

    @Override
    public void updateCategory(UpdateCategoryRequest categoryRequest){
        log.info("Start updating category with name {}", categoryRequest.newName());
        Category category = categoryRepository.findByCategoryId(categoryRequest.categoryId());
        if (category != null){
            category.setName(categoryRequest.newName());
            categoryRepository.save(category);
        }
        else {
            log.error("Category not found with categoryId {}", categoryRequest.categoryId());
            throw new CategoryNotFoundException(categoryRequest.categoryId());
        }
    }
}
