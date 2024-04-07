package com.bfu.cartservice.controller.payload;

import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;

import java.util.List;
import java.util.UUID;

public record CartResponse(UUID id,
        String userId,
        List<Product>products)
{
    public static CartResponse from(Cart cart){
        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getProducts()
        );
    }
}
