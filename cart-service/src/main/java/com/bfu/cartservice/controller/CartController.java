package com.bfu.cartservice.controller;

import com.bfu.cartservice.client.CatalogueClient;
import com.bfu.cartservice.controller.payload.AddProductToCartPayload;
import com.bfu.cartservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.service.CartService;
import com.bfu.cartservice.util.BindingChecker;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CartController {
    private final MessageSource messageSource;
    private final CartService cartService;
    private final CatalogueClient client;

    @GetMapping("/cart")
    public ArrayOfSimplifiedProduct getCart(Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/cart")
    @BindingChecker
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddProductToCartPayload payload,
                                       Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        SimplifiedProductResponse cartProductResponse = client.getProductInfo(payload.productId());
        Product product = Product.builder()
                .id(cartProductResponse.productId())
                .name(cartProductResponse.name())
                .price(cartProductResponse.price())
                .quantity(payload.quantity()).build();
        cartService.addToCart(userId, product);
        return ResponseEntity.ok("Product added to cart");
    }

    @PatchMapping("/cart/increase")
    public ResponseEntity<?> increaseProductQuantity(@RequestParam String productId, Principal principal) {
        SimplifiedProductResponse cartProductResponse = client.getProductInfo(productId);
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.increaseProductQuantity(userId, cartProductResponse.productId(), cartProductResponse.price());
        return ResponseEntity.ok("Product quantity increased");
    }

    @PatchMapping("/cart/reduce")
    public ResponseEntity<?> reduceProductQuantity(@RequestParam String productId, Principal principal) {
        SimplifiedProductResponse cartProductResponse = client.getProductInfo(productId);
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.reduceProductQuantity(userId, cartProductResponse.productId(), cartProductResponse.price());
        return ResponseEntity.ok("Product quantity reduced");
    }


    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable String productId, Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.deleteProductFromCart(userId, productId);
        return ResponseEntity.ok("Product deleted from cart");
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteAllProducts(Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        cartService.deleteAllProducts(userId);
        return ResponseEntity.ok("Cart cleared");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        Objects.requireNonNull(this.messageSource.getMessage(exception.getMessage(), new Object[0],
                                exception.getMessage(), locale))));
    }
}
