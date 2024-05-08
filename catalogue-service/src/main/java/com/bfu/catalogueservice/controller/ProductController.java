package com.bfu.catalogueservice.controller;


import com.bfu.catalogueservice.client.FavouriteProductsClient;
import com.bfu.catalogueservice.controller.payload.Product.*;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        List<Product> products = productService.getAllProducts();
        return products
                .stream()
                .map(p->FullProductResponse.from(p, productPhotoController.getProductPhotos(p.getProductId())))
                .toList();
    }

    @GetMapping("/simplified-product-info")
    public SimplifiedProductResponse getSimpleProductById(@RequestParam String productId){
        String photo = productPhotoController.getPreviewPhoto(productId);
        return productService.getSimpleProductById(productId, photo);
    }
    @GetMapping("/full-product-info")
    public FullProductResponse getFullProductById(@RequestParam String productId){
        List<String> photos = productPhotoController.getProductPhotos(productId);
        return productService.getFullProductById(productId, photos);
    }
    @GetMapping("/simplified-products-info")
    public ResponseEntity<?> getArraySimpleProductsById(){
        ArrayOfProductsIdRequest array = client.getProductsIdByUserId();
        List<Product> products = productService.getArraySimpleProductsById(array.productsId());
        log.info("Getting Array Simplified products where productId in {}", array.productsId());
        return ResponseEntity.ok(
                products.stream()
                        .map(p->SimplifiedProductResponse.from(p,
                                productPhotoController.getPreviewPhoto(p.getProductId())))
                        .toList()
        );
    }
    @GetMapping("/full-products-info")
    public ResponseEntity<?> getArrayFullProductById(){
        ArrayOfProductsIdRequest array = client.getProductsIdByUserId();
        List<Product> products = productService.getArrayFullProductsById(array.productsId());
        log.info("Getting Array Full products where productId in {}", array.productsId());
        return ResponseEntity.ok(
                        products.stream()
                                .map(p->FullProductResponse.from(p,
                                        productPhotoController.getProductPhotos(p.getProductId())))
                                .toList()
        );
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
