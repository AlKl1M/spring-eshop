package com.bfu.catalogueservice.repository;

import com.bfu.catalogueservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(String productId);
}
