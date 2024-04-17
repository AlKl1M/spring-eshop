package com.bfu.catalogueservice.exception;

public class BrandNotFoundException extends RuntimeException{
    public BrandNotFoundException(String brandId) {
        super(String.format("Brand with id=%d not found", brandId));
    }

    public static BrandNotFoundException of(String brandId) {
        return new BrandNotFoundException(brandId);
    }
}
