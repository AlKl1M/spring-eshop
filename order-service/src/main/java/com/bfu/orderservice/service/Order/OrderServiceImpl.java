package com.bfu.orderservice.service.Order;


import com.bfu.orderservice.controller.payload.ChangeStatusRequest;
import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.Status;
import com.bfu.orderservice.exceptions.OrderNotFoundException;
import com.bfu.orderservice.exceptions.StatusNotFoundException;
import com.bfu.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    @Override
    public Order createOrder(String userId) {
        return orderRepository.save(Order.builder()
                .orderId(String.format("%016d", new BigInteger(UUID.randomUUID().toString()
                        .replace("-", "").substring(0,15), 16)))
                .userId(userId)
                .date(Timestamp.from(Instant.now()))
                .status(Status.CREATED)
                .build());
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        log.info("Start to getting orders by userId");
        Optional<List<Order>> orders = orderRepository.findAllByUserId(userId);
        if (orders.isPresent()){
            log.info("Orders by userId has been gotten");
            return orders.get();
        }
        throw OrderNotFoundException.ofUser(userId);
    }

    @Override
    public void deleteOrder(String orderId) {
        Optional<Order> order = orderRepository.findByOrderId(orderId);
        order.ifPresent(orderRepository::delete);
        log.info("Order has been deleted");
    }

    @Override
    public void changeStatus(ChangeStatusRequest changeStatusRequest){
        log.info("Start to change status");
        Optional<Order> optionalOrder = orderRepository.findByOrderId(changeStatusRequest.orderId());
        if (optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            if (!order.getStatus().name().equals(changeStatusRequest.status())){
                order.setStatus(Status.valueOf(changeStatusRequest.status()));
                orderRepository.save(order);
                log.info("Order status has been changed");
            }
            throw StatusNotFoundException.of(changeStatusRequest.status());
        }
        throw OrderNotFoundException.ofOrder(changeStatusRequest.orderId());
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        log.info("Start to getting order by orderId");
        Optional<Order> optionalOrder = orderRepository.findByOrderId(orderId);
        if (optionalOrder.isPresent()){
            log.info("Order by orderId has been gotten");
            return optionalOrder.get();
        }
        throw OrderNotFoundException.ofOrder(orderId);
    }
}
