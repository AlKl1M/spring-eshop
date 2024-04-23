package com.bfu.orderservice.controller.payload;

import java.util.List;

public record AddProductsToOrderRequest(
        String orderId,
        List<SimplifiedProductResponse> products
) {
}
