package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.CartPayload;
import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;

    @Override
    public List<CartPayload> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(CartPayload::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Cart createCartByUserId(String userId) {
        Cart cart = new Cart(UUID.randomUUID(), userId, Collections.emptyList(), BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addToCart(String userId, Product newProduct) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartByUserId(userId));
        List<Product> products = cart.getProducts();
        Optional<Product> existingProduct = products.stream()
                .filter(product -> product.getId().equals(newProduct.getId()))
                .findFirst();
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setQuantity(product.getQuantity() + 1);
            product.setPrice(product.getPrice().add(newProduct.getPrice()));
        } else {
            newProduct.setQuantity(1);
            products.add(newProduct);
        }
        cart.setTotalPrice(cart.getTotalPrice().add(newProduct.getPrice()));
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void increaseProductQuantity(String userId, String productId, BigDecimal price) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Product> products = cart.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setQuantity(product.getQuantity() + 1);
                product.setPrice(product.getPrice().add(price));
                cart.setTotalPrice(cart.getTotalPrice().add(price));
                break;
            }
        }
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void reduceProductQuantity(String userId, String productId, BigDecimal price) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<Product> products = cart.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                int newQuantity = product.getQuantity() - 1;
                BigDecimal newPrice = product.getPrice().subtract(price);
                BigDecimal newTotalPrice = cart.getTotalPrice().subtract(price);
                product.setQuantity(newQuantity >= 0 ? newQuantity : 0);
                product.setPrice(newPrice.compareTo(BigDecimal.ZERO) >= 0 ? newPrice : BigDecimal.ZERO);
                cart.setTotalPrice(newTotalPrice.compareTo(BigDecimal.ZERO) >= 0 ? newTotalPrice : BigDecimal.ZERO);
                break;
            }
        }
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteAllProducts(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setProducts(new ArrayList<>());
        cartRepository.save(cart);
    }
}
