package com.bfu.catalogueservice.controller.payload.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @Size(max = 15, min = 15)
        String categoryId,
        @NotBlank
        @NotNull
        @Size(max = 20)
        String newName
) {
}
