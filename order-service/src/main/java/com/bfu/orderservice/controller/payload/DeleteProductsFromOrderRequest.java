package com.bfu.orderservice.controller.payload;

import java.util.List;

public record DeleteProductsFromOrderRequest(
        String orderId,
        List<String> productsId
) {
}
