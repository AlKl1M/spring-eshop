package com.bfu.orderservice.service.Order;

import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.orderservice.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(String userId);
    List<Order> getOrder(String userId);

    void deleteOrder(String orderId);
}
