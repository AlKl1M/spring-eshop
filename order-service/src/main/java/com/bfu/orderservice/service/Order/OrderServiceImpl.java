package com.bfu.orderservice.service.Order;


import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.orderservice.controller.payload.SimplifiedProductResponse;
import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.OrderProduct;
import com.bfu.orderservice.entity.Status;
import com.bfu.orderservice.exceptions.OrderNotFoundException;
import com.bfu.orderservice.repository.OrderProductRepository;
import com.bfu.orderservice.repository.OrderRepository;
import com.bfu.orderservice.service.OrderProduct.OrderProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    @Override
    public Order createOrder(String userId) {
        String orderId = String.format("%016d", new BigInteger(UUID.randomUUID().toString()
                .replace("-", "").substring(0,15), 16));
        Order order = orderRepository.save(Order.builder()
                .orderId(orderId)
                .userId(userId)
                .date("12")
                .status("Created")
                .build());
        return order;
    }

    @Override
    public List<Order> getOrder(String userId) {
        Optional<List<Order>> orders = orderRepository.findAllByUserId(userId);
        if (orders.isPresent()){
            return orders.get();
        }
        throw new OrderNotFoundException(userId);
    }

    @Override
    public void deleteOrder(String orderId) {
        log.info("Start to delete order");
        Optional<Order> order = orderRepository.findByOrderId(orderId);
        order.ifPresent(orderRepository::delete);
    }
}
