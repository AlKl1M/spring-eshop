package com.bfu.catalogueservice.service.product;

import com.bfu.catalogueservice.controller.payload.Product.CreateProductRequest;
import com.bfu.catalogueservice.controller.payload.Product.DeleteProductRequest;
import com.bfu.catalogueservice.controller.payload.Product.ProductResponse;
import com.bfu.catalogueservice.controller.payload.Product.UpdateProductRequest;
import com.bfu.catalogueservice.entity.Product;

import java.util.List;

public interface ProductService {
    void createProduct(CreateProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    void updateProduct(UpdateProductRequest productRequest);

    void deleteProduct(DeleteProductRequest productRequest);
}
