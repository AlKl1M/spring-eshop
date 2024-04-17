package com.bfu.catalogueservice.controller.payload.Product;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Map;

public record CreateProductRequest(
        @NotBlank
        @NotNull
        @Size(max = 20)
        String name,
        @NotNull
        @Min(0)
        BigDecimal price,
        JsonNode attributes,
        String description,
        @Size(max = 15, min = 15)
        @NotNull
        @NotBlank
        String brandId,
        @Size(max = 15, min = 15)
        @NotNull
        @NotBlank
        String categoryId
) {
}
