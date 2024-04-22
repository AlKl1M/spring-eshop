package com.bfu.orderservice.controller.payload;

import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.OrderProduct;
import com.bfu.orderservice.entity.Status;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        String orderId,
        String date,
        Status status,
        List<SimplifiedProductResponse> products
) {
    public static OrderResponse from(Order order, List<SimplifiedProductResponse> products){
        return new OrderResponse(
                order.getOrderId(),
                order.getDate(),
                order.getStatus(),
                products
        );
    }
}
