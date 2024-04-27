package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;

import java.math.BigDecimal;

public interface CartService {
    ArrayOfSimplifiedProduct getCartByUserId(String userId);
    Cart createCartByUserId(String userId);
    void addToCart(String userId, Product newProduct);
    void increaseProductQuantity(String userId, String productId, BigDecimal price);
    void reduceProductQuantity(String userId, String productId, BigDecimal price);
    void deleteProductFromCart(String userId, String productId);
    void deleteAllProducts(String userId);
}
