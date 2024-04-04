package com.bfu.catalogueservice.controller;


import com.bfu.catalogueservice.controller.payload.Product.CreateProductRequest;
import com.bfu.catalogueservice.controller.payload.Product.DeleteProductRequest;
import com.bfu.catalogueservice.controller.payload.Product.ProductResponse;
import com.bfu.catalogueservice.controller.payload.Product.UpdateProductRequest;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
public class ProductController {

    private final ProductService productService;
    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest productRequest){
        productService.createProduct(productRequest);
        return ResponseEntity.ok("Product has been created");
    }
    @GetMapping("/getAllProducts")
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @PutMapping("/update-product")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductRequest productRequest){
        productService.updateProduct(productRequest);
        return ResponseEntity.ok("Product has been updated");
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestBody DeleteProductRequest productRequest) {
        productService.deleteProduct(productRequest);
        return ResponseEntity.noContent().build();
    }
}
