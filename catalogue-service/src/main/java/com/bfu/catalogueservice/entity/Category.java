package com.bfu.catalogueservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(schema = "catalogue", name = "t_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_category_id")
    private String categoryId;

    @Column(name = "c_title")
    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products;
}
