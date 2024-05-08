package com.bfu.catalogueservice.controller.payload.ProductPhoto;

import com.bfu.catalogueservice.entity.Product;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public record CreateProductPhotoResponse(
        Product product,
        List<BufferedImage> images
) {
    public static CreateProductPhotoResponse from(Product product, List<BufferedImage> images){
        return new CreateProductPhotoResponse(product, images);
    }
}
