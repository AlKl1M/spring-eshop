package com.bfu.cartservice.controller.payload;

import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;

import java.util.List;
import java.util.UUID;

public record CartPayload(UUID id,
        List<Product>products)
{
    public static CartPayload from(Cart cart){
        return new CartPayload(
                cart.getId(),
                cart.getProducts()
        );
    }
}
