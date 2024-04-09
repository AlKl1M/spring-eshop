package com.bfu.catalogueservice.controller.payload.Product;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @Size(max = 15, min = 15)
        @NotNull
        @NotBlank
        String productId,
        @NotBlank
        @NotNull
        @Size(max = 20)
        String newName,
        @NotBlank
        @NotNull
        @Min(0)
        BigDecimal newPrice,
        JsonNode attributes,
        String description,
        @Size(max = 15, min = 15)
        @NotNull
        @NotBlank
        String newBrandId,
        @Size(max = 15, min = 15)
        @NotNull
        @NotBlank
        String newCategoryId
) {
}
