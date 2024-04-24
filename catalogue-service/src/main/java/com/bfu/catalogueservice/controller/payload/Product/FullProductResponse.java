package com.bfu.catalogueservice.controller.payload.Product;

import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public record FullProductResponse(
        String productId,
        String name,
        BigDecimal price,
        JsonNode attributes,
        String description,
        Category category,
        Brand brand
) {
    public static FullProductResponse from(Product product){
        return new FullProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getAttributes(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand()
        );
    }
}
