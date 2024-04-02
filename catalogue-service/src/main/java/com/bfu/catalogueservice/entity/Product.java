package com.bfu.catalogueservice.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "catalogue", name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_product_id")
    private UUID productId;

    @Column(name = "c_title")
    private String title;

    @Column(name = "c_price")
    private BigDecimal price;

    @Column(name = "c_attributes", columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode attributes;

    @ManyToOne
    @JoinColumn(name = "c_category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "c_brand_id")
    private Brand brand;
}
