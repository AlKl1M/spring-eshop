package com.bfu.catalogueservice.controller;


import com.bfu.catalogueservice.controller.payload.Product.*;
import com.bfu.catalogueservice.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
@Slf4j
public class ProductController {

    private final ProductService productService;
    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest productRequest){
        productService.createProduct(productRequest);
        log.info("Product has been created");
        return ResponseEntity.ok("Product has been created");
    }
    @GetMapping("/getAllProducts")
    public List<FullProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/simplified-product-info/{productId}")
    public SimplifiedProductResponse getSimpleProductById(@PathVariable String productId){
        return productService.getSimpleProductById(productId);
    }
    @GetMapping("/full-product-info")
    public FullProductResponse getFullProductById(@RequestParam String productId){
        return productService.getFullProductById(productId);
    }

    @PutMapping("/update-product")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductRequest productRequest){
        productService.updateProduct(productRequest);
        log.info("Product has been updated");
        return ResponseEntity.ok("Product has been updated");
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestParam String productId) {
        productService.deleteProduct(productId);
        log.info("Product has been deleted");
        return ResponseEntity.noContent().build();
    }
}
