package com.bfu.catalogueservice.controller.payload.Product;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record CreateProductRequest(
        String name,
        BigDecimal price,
//        Map<String, String> attributes,
        String brandId,
        String categoryId
) {
}
