package com.bfu.catalogueservice.controller;


import com.bfu.catalogueservice.client.FavouriteProductsClient;
import com.bfu.catalogueservice.controller.payload.Product.*;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.service.product.ProductService;
import com.bfu.catalogueservice.service.product_photo.ProductPhotoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final FavouriteProductsClient client;
    private final ProductPhotoController productPhotoController;
    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest productRequest) throws IOException {
        CreateProductPhotoResponse productPhotoResponse = productService.createProduct(productRequest);
        log.info("Product has been created");
        productPhotoController.createProductPhoto(productPhotoResponse);
        return ResponseEntity.ok("Product has been created");
    }
    @GetMapping("/getAllProducts")
    public List<FullProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/simplified-product-info")
    public SimplifiedProductResponse getSimpleProductById(@RequestParam String productId){
        return productService.getSimpleProductById(productId);
    }
    @GetMapping("/full-product-info")
    public FullProductResponse getFullProductById(@RequestParam String productId){
        return productService.getFullProductById(productId);
    }
    @GetMapping("/simplified-products-info")
    public List<SimplifiedProductResponse> getArraySimpleProductsById(){
        ArrayOfProductsIdRequest array = client.getProductsIdByUserId();
        return productService.getArraySimpleProductsById(array.productsId());
    }
    @GetMapping("/full-products-info")
    public List<FullProductResponse> getArrayFullProductById(){
        ArrayOfProductsIdRequest array = client.getProductsIdByUserId();
        return productService.getArrayFullProductsById(array.productsId());
    }

    @GetMapping("/is-exists")
    public ResponseEntity<?> isProductExist(@RequestParam String productId){
        return ResponseEntity.ok(productService.isProductExists(productId));
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
