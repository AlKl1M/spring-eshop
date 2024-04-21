package com.bfu.orderservice.controller.payload;

import com.bfu.orderservice.entity.OrderProduct;

import java.math.BigDecimal;

public record SimplifiedProductResponse(
        String productId,
        String name,
        int quantity,
        BigDecimal price
) {
    public static SimplifiedProductResponse from(OrderProduct orderProduct){
        return new SimplifiedProductResponse(
                orderProduct.getProductId(),
                orderProduct.getName(),
                orderProduct.getQuantity(),
                orderProduct.getPrice()
        );
    }
}
