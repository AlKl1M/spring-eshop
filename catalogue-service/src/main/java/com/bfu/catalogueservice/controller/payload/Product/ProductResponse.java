package com.bfu.catalogueservice.controller.payload.Product;

import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public record ProductResponse(
        String productId,
        String name,
        BigDecimal price,
//        JsonNode attributes,
        Category category,
        Brand brand
) {
    public static ProductResponse from(Product product){
        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
//                product.getAttributes(),
                product.getCategory(),
                product.getBrand()
        );
    }
}
