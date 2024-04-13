package com.bfu.cartservice.controller.payload;

import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartPayload(UUID id,
                          List<Product>products,
                          BigDecimal totalPrice)
{
    public static CartPayload from(Cart cart){
        return new CartPayload(
                cart.getId(),
                cart.getProducts(),
                cart.getTotalPrice()
        );
    }
}
