package com.bfu.catalogueservice.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String productId){
        super(String.format("Product with id=%d not found", productId));
    }

    public static ProductNotFoundException of(String productId) {
        return new ProductNotFoundException(productId);
    }

}
