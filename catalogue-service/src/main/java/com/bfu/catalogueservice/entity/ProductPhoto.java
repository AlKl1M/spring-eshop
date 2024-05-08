package com.bfu.catalogueservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(schema = "catalogue", name = "t_product_photo")
public class ProductPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_photo_id")
    private String photoId;

    @JoinColumn(name = "c_product_id")
    @ManyToOne
    private Product product;

    @Column(name = "c_is_preview")
    private boolean preview;

    @Column(name = "c_url")
    private String url;
}
