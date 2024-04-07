package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.CartResponse;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;

import java.util.List;
import java.util.UUID;

public interface CartService {
    List<CartResponse> getAllCarts();
    void createCartByUserId(String userId);
    Cart addToCart(String userId, Product newProduct);
    void increaseProductQuantity(UUID cartId, String productId);
    void reduceProductQuantity(UUID cartId, String productId);
    void deleteAllProducts(UUID cartId);

}
