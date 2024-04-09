package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.CartPayload;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;

    @Override
    public List<CartPayload> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(CartPayload::from)
                .collect(Collectors.toList());
    }

    @Override
    public Cart createCartByUserId(String userId) {
        Cart cart = new Cart(UUID.randomUUID(), userId, Collections.emptyList());
        return cartRepository.save(cart);
    }

    @Override
    public void addToCart(String userId, Product newProduct) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart = optionalCart.orElseGet(() -> createCartByUserId(userId));

        List<Product> products = cart.getProducts();
        boolean found = products.stream()
                .filter(product -> product.getId().equals(newProduct.getId()))
                .peek(product -> product.setQuantity(product.getQuantity() + 1))
                .findFirst()
                .isPresent();

        if (!found) {
            newProduct.setQuantity(1);
            products.add(newProduct);
        }

        cartRepository.save(cart);
    }

    @Override
    public void increaseProductQuantity(UUID cartId, String productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Product> products = cart.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setQuantity(product.getQuantity() + 1);
                break;
            }
        }
        cartRepository.save(cart);
    }

    public void reduceProductQuantity(UUID cartId, String productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Product> products = cart.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                int newQuantity = product.getQuantity() - 1;
                product.setQuantity(newQuantity >= 0 ? newQuantity : 0);
                break;
            }
        }
        cartRepository.save(cart);
    }

    @Override
    public void deleteAllProducts(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setProducts(new ArrayList<>());
        cartRepository.save(cart);
    }
}
