package com.bfu.catalogueservice.controller.payload.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank
        @NotNull
        @Size(max = 20)
        String name
) {
}
