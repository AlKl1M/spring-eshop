package com.bfu.catalogueservice.repository;

import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);
}
