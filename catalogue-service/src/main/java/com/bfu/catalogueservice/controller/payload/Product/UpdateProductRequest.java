package com.bfu.catalogueservice.controller.payload.Product;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String productId,
        String newName,
        BigDecimal newPrice,
//        JsonNode attributes,
        String newBrandId,
        String newCategoryId
) {
}
