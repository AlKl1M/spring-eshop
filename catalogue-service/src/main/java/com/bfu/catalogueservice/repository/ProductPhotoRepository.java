package com.bfu.catalogueservice.repository;

import com.bfu.catalogueservice.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
}
