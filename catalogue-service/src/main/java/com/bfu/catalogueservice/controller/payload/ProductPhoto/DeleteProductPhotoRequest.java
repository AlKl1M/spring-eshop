package com.bfu.catalogueservice.controller.payload.ProductPhoto;

import java.util.List;

public record DeleteProductPhotoRequest(
        String productId,
        List<String> photosId
) {
}
