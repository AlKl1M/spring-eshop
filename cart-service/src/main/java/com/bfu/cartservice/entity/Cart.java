package com.bfu.cartservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("cart")
public class Cart {
    @Id
    private UUID id;
    private String userId;
    private List<Product> products;
    BigDecimal totalPrice;
}
