package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.CartResponse;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;

    @Override
    public List<CartResponse> getAllCarts() {
        List<CartResponse> carts = new ArrayList<>();
        for (Cart cart: cartRepository.findAll()){
            carts.add(CartResponse.from(cart));
        }
        return carts;
    }

    @Override
    public void createCartByUserId(String userId) {
        Cart cart = new Cart(UUID.randomUUID(), userId, Collections.emptyList());
        cartRepository.save(cart);
    }

    @Override
    public Cart addToCart(String userId, Product newProduct) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            createCartByUserId(userId);
            Optional<Cart> newCart = cartRepository.findByUserId(userId);
            cart = newCart.get();
        }

        List<Product> products = cart.getProducts();
        boolean found = false;

        for (Product product : products) {
            if (product.getId().equals(newProduct.getId())) {
                product.setQuantity(product.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            newProduct.setQuantity(1);
            products.add(newProduct);
        }

        cart.setProducts(products);
        return cartRepository.save(cart);
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

    @Override
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
