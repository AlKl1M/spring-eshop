package com.bfu.orderservice.repository;

import com.bfu.orderservice.entity.Order;
import com.bfu.orderservice.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findAllByUserId(String userId);
    Optional<Order> findByOrderId(String orderId);

    @Query("SELECT o FROM Order o WHERE (o.status = :status)")
    List<Order> getOrdersByStatus(@Param("status") Status status);
}
