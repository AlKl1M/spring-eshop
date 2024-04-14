package com.bfu.catalogueservice.service.product;

import com.bfu.catalogueservice.controller.payload.Product.*;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    void createProduct(CreateProductRequest productRequest);

    List<FullProductResponse> getAllProducts();

    void updateProduct(UpdateProductRequest productRequest);

    void deleteProduct(String productId);

    SimplifiedProductResponse getSimpleProductById(String productId);

    FullProductResponse getFullProductById(String productId);

    ArrayList<SimplifiedProductResponse> getArraySimpleProductsById(ArrayList<String> productsId);

    ArrayList<FullProductResponse> getArrayFullProductsById(ArrayList<String> productsId);
}
