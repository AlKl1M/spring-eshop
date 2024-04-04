package com.bfu.catalogueservice.controller.payload.Brand;

public record UpdateBrandRequest(
        String newName,
        String brandId
) {
}
