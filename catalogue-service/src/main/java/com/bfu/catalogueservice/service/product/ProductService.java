package com.bfu.catalogueservice.service.product;

import com.bfu.catalogueservice.controller.payload.Product.*;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.entity.Product;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    CreateProductPhotoResponse createProduct(CreateProductRequest productRequest);

    List<Product> getAllProducts();

    void updateProduct(UpdateProductRequest productRequest);

    void deleteProduct(String productId);

    SimplifiedProductResponse getSimpleProductById(String productId, String photos);

    FullProductResponse getFullProductById(String productId, List<String> photos);

    List<Product> getArraySimpleProductsById(List<String> productsId);

    List<Product> getArrayFullProductsById(List<String> productsId);

    boolean isProductExists(String productId);
}
