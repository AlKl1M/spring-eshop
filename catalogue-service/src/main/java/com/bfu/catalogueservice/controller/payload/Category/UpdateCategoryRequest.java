package com.bfu.catalogueservice.controller.payload.Category;

public record UpdateCategoryRequest(
        String categoryId,
        String newName
) {
}
