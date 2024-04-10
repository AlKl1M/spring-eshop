package com.bfu.cartservice.controller;

import com.bfu.cartservice.client.CartServiceClient;
import com.bfu.cartservice.controller.payload.CartPayload;
import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartServiceClient client;

    @GetMapping("/carts")
    public List<CartPayload> getAllCarts(){
        return cartService.getAllCarts();
    }

    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable String cartId, @PathVariable String productId, Principal principal) {
        SimplifiedProductResponse cartProductResponse = client.getProductInfo(productId);
        Product product = Product.builder()
                .id(cartProductResponse.productId())
                .name(cartProductResponse.name())
                .price(cartProductResponse.price())
                .quantity(1).build();
        cartService.addToCart(cartId, product);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cartId}/products/{productId}/increase")
    public ResponseEntity<?> increaseProductQuantity(@PathVariable UUID cartId, @PathVariable String productId) {
        cartService.increaseProductQuantity(cartId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cartId}/products/{productId}/reduce")
    public ResponseEntity<?> reduceProductQuantity(@PathVariable UUID cartId, @PathVariable String productId) {
        cartService.reduceProductQuantity(cartId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/products")
    public ResponseEntity<?> deleteAllProducts(@PathVariable UUID cartId) {
        cartService.deleteAllProducts(cartId);
        return ResponseEntity.ok().build();
    }
}
