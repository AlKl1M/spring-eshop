package com.bfu.orderservice.repository;

import com.bfu.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findAllByUserId(String userId);
    Optional<Order> findByOrderId(String orderId);
}
