package com.bfu.catalogueservice.service.product_photo;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import java.io.IOException;

public interface ProductPhotoService {
    void createProductPhoto(CreateProductPhotoResponse productPhotoResponse) throws IOException;
}
