package com.bfu.catalogueservice.repository;

import com.bfu.catalogueservice.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandId(String brandId);
}
