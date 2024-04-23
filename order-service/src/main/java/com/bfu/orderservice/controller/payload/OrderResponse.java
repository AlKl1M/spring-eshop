package com.bfu.orderservice.controller.payload;

import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.OrderProduct;
import com.bfu.orderservice.entity.Status;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public record OrderResponse(
        String orderId,
        Timestamp date,
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
