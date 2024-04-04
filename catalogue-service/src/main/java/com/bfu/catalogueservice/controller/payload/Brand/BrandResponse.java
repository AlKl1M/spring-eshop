package com.bfu.catalogueservice.controller.payload.Brand;

import com.bfu.catalogueservice.entity.Brand;

public record BrandResponse(
        String brandId,
        String name
) {
    public static BrandResponse from(Brand brand){
        return new BrandResponse(
                brand.getBrandId(),
                brand.getName()
        );
    }
}
