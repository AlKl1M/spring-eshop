package com.bfu.orderservice.entity;

import com.bfu.orderservice.controller.payload.SimplifiedProductResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "order_service", name = "t_order_products")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_product_id")
    private String productId;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_quantity")
    private int quantity;

    @Column(name = "c_price")
    private BigDecimal price;

    @JoinColumn(name = "c_order")
    @ManyToOne
    private Order order;

    public static OrderProduct from(SimplifiedProductResponse simplifiedProductResponse, Order order){
        return OrderProduct.builder()
                .productId(simplifiedProductResponse.productId())
                .name(simplifiedProductResponse.name())
                .quantity(simplifiedProductResponse.quantity())
                .price(simplifiedProductResponse.price())
                .order(order)
                .build();
    }
}
