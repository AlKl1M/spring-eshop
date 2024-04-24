package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;

    @Override
    public ArrayOfSimplifiedProduct getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .map(cart -> {
                    List<SimplifiedProductResponse> simplifiedProducts = cart.getProducts().stream()
                            .map(product -> new SimplifiedProductResponse(product.getId(), product.getName(), product.getQuantity(), product.getPrice()))
                            .toList();
                    return new ArrayOfSimplifiedProduct(simplifiedProducts);
                })
                .orElseGet(() -> new ArrayOfSimplifiedProduct(Collections.emptyList()));
    }

    @Override
    @Transactional
    public Cart createCartByUserId(String userId) {
        log.info("User with id {} trying to create a cart", userId);
        Cart cart = new Cart(UUID.randomUUID(), userId, Collections.emptyList(), BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addToCart(String userId, Product newProduct) {
        log.info("User {} trying to add product {} to cart", userId, newProduct.getId());
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartByUserId(userId));
        List<Product> products = cart.getProducts();
        Optional<Product> existingProduct = products.stream()
                .filter(product -> product.getId().equals(newProduct.getId()))
                .findFirst();
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setQuantity(product.getQuantity() + newProduct.getQuantity());
            product.setPrice(product.getPrice().add(newProduct.getPrice().multiply(BigDecimal.valueOf(newProduct.getQuantity()))));
        } else {
            newProduct.setPrice(newProduct.getPrice().multiply(BigDecimal.valueOf(newProduct.getQuantity())));
            products.add(newProduct);
        }
        cart.setTotalPrice(cart.getTotalPrice().add(newProduct.getPrice()));
        cartRepository.save(cart);
        log.info("User {} added product to cart successfully!", userId);
    }

    @Override
    @Transactional
    public void increaseProductQuantity(String userId, String productId, BigDecimal price) {
        log.info("User {} trying to increase product quantity in his cart", userId);
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
        log.info("User {} has successfully increased product quantity", userId);
    }

    @Override
    @Transactional
    public void reduceProductQuantity(String userId, String productId, BigDecimal price) {
        log.info("User {} trying to reduce product quantity in his cart", userId);
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        Optional<Product> optionalProduct = cart.getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
        optionalProduct.ifPresent(product -> {
            int newQuantity = Math.max(product.getQuantity() - 1, 0);
            BigDecimal newPrice = product.getPrice().subtract(price).max(BigDecimal.ZERO);
            BigDecimal newTotalPrice = cart.getTotalPrice().subtract(price).max(BigDecimal.ZERO);

            if (newQuantity == 0) {
                cart.getProducts().remove(product);
            } else {
                product.setQuantity(newQuantity);
                product.setPrice(newPrice);
            }
            cart.setTotalPrice(newTotalPrice);
        });
        cartRepository.save(cart);
        log.info("User {} has successfully reduced product quantity", userId);
    }

    @Override
    @Transactional
    public void deleteProductFromCart(String userId, String productId) {
        log.info("Deleting product {} from cart of user {}", productId, userId);
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getProducts().removeIf(product -> product.getId().equals(productId));
            BigDecimal totalPrice = cart.getProducts().stream()
                    .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalPrice(totalPrice);
            cartRepository.save(cart);
            log.info("Product {} deleted successfully from cart of user {}", productId, userId);
        });
    }

    @Override
    @Transactional
    public void deleteAllProducts(String userId) {
        log.info("User {} trying to delete all products from cart", userId);
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setProducts(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
        log.info("User {} has successfully deleted  all products from his cart", userId);
    }
}
