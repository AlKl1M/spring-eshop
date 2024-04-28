package com.bfu.catalogueservice.service.product_photo;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.DeleteProductPhotoRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ProductPhotoService {
    void createProductPhoto(CreateProductPhotoResponse productPhotoResponse) throws IOException;

    List<String> getProductPhotos(String productId);

    void deleteProductPhoto(DeleteProductPhotoRequest request) throws IOException;
}
