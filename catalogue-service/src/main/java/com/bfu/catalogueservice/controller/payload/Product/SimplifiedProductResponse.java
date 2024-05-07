package com.bfu.catalogueservice.controller.payload.Product;

import com.bfu.catalogueservice.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public record SimplifiedProductResponse(
        String productId,
        String name,
        BigDecimal price,
        String preview
) {
    public static SimplifiedProductResponse from(Product p, String preview) {
        return new SimplifiedProductResponse(
                p.getProductId(),
                p.getName(),
                p.getPrice(),
                preview
        );
    }
}
