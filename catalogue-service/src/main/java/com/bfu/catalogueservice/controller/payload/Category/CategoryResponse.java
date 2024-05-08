package com.bfu.catalogueservice.controller.payload.Category;

import com.bfu.catalogueservice.entity.Category;

public record CategoryResponse(
        String categoryId,
        String name
) {
    public static CategoryResponse from(Category category){
        return new CategoryResponse(
                category.getCategoryId(),
                category.getName()
                );
    }
}
