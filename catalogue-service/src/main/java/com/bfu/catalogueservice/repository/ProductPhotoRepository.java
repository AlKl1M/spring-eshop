package com.bfu.catalogueservice.repository;

import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long>{
    Optional<List<ProductPhoto>> findAllByProductId(Long id);

    Optional<List<ProductPhoto>> findAllByProduct(Product byProductId);
}
