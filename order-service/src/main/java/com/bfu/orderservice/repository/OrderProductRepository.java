package com.bfu.orderservice.repository;

import com.bfu.orderservice.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<List<OrderProduct>> findAllByOrder_OrderId(String orderId);
    Optional<List<OrderProduct>> findAllByOrder_OrderIdAndProductIdIn(String order_id, List<String> productId);
}
