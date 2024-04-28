package com.bfu.catalogueservice.service.image;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.DeleteProductPhotoRequest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<BufferedImage> createBufferedImages(List<byte[]> images);

    String createImage(BufferedImage bufferedImage, boolean flag, String productId) throws IOException;

    String getPathByPhotoId(String photoId);

    void deletePhoto(DeleteProductPhotoRequest request) throws IOException;
}
