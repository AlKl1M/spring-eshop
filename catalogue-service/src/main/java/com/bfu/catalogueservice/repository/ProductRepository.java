package com.bfu.catalogueservice.repository;

import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);

    List<Product> findAllByProductIdIn(List<String> productId);
    List<Product> findAllByBrand(Optional<Brand> brand);
    List<Product> findAllByCategory(Optional<Category> category);

    boolean existsProductByProductId(String productId);
}
