package com.bfu.cartservice.controller;

import com.bfu.cartservice.client.CartServiceClient;
import com.bfu.cartservice.controller.payload.CartPayload;
import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @PostMapping("/products/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable String productId, Principal principal) {
        SimplifiedProductResponse cartProductResponse = client.getProductInfo(productId);
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        Product product = Product.builder()
                .id(cartProductResponse.productId())
                .name(cartProductResponse.name())
                .price(cartProductResponse.price())
                .quantity(1).build();
        cartService.addToCart(userId, product);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/{productId}/increase")
    public ResponseEntity<?> increaseProductQuantity(@PathVariable String productId, Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.increaseProductQuantity(userId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/{productId}/reduce")
    public ResponseEntity<?> reduceProductQuantity(@PathVariable String productId, Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.reduceProductQuantity(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products")
    public ResponseEntity<?> deleteAllProducts(Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.deleteAllProducts(userId);
        return ResponseEntity.ok().build();
    }
}
