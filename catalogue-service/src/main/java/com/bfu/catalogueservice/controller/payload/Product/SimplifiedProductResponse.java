package com.bfu.catalogueservice.controller.payload.Product;

import java.math.BigDecimal;

public record SimplifiedProductResponse(
        String productId,
        String name,
        BigDecimal price
) {
}
