package com.bfu.cartservice.controller;

import com.bfu.cartservice.controller.payload.CartProductResponse;
import com.bfu.cartservice.client.CatalogueServiceClient;
import com.bfu.cartservice.controller.payload.CartResponse;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CatalogueServiceClient catalogueServiceClient;

    @GetMapping("/carts")
    public List<CartResponse> getAllCarts(){
        return cartService.getAllCarts();
    }

    @PostMapping("/{cartId}/products/{productId}")
    public Cart addToCart(@PathVariable String cartId, @PathVariable String productId) {
        CartProductResponse cartProductResponse = catalogueServiceClient.getProductById(productId);
        Product product = Product.builder()
                .id(cartProductResponse.id())
                .name(cartProductResponse.name())
                .price(cartProductResponse.price())
                .quantity(1).build();
        return cartService.addToCart(cartId, product);
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
