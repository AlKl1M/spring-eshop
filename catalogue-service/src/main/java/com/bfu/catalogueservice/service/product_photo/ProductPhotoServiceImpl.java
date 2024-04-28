package com.bfu.catalogueservice.service.product_photo;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.DeleteProductPhotoRequest;
import com.bfu.catalogueservice.entity.ProductPhoto;
import com.bfu.catalogueservice.exception.ProductNotFoundException;
import com.bfu.catalogueservice.repository.ProductPhotoRepository;
import com.bfu.catalogueservice.service.image.ImageService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductPhotoServiceImpl implements ProductPhotoService{
    private final ProductPhotoRepository productPhotoRepository;
    private final ImageService imageService;
    @Override
    @Transactional
    public void createProductPhoto(CreateProductPhotoResponse productPhotoResponse) throws IOException {
        boolean flag = true;
        for (BufferedImage bufferedImage: productPhotoResponse.images()){
            if (flag) {
                String photoId = imageService.createImage(bufferedImage, flag, productPhotoResponse.product().getProductId());
                productPhotoRepository.save(
                        ProductPhoto.builder()
                                .isPreview(flag)
                                .product(productPhotoResponse.product())
                                .photoId(photoId)
                                .url(imageService.getPathByPhotoId(photoId))
                                .build()
                );
                flag = false;
            }
            String photoId = imageService.createImage(bufferedImage, flag, productPhotoResponse.product().getProductId());
            productPhotoRepository.save(
                    ProductPhoto.builder()
                            .isPreview(flag)
                            .product(productPhotoResponse.product())
                            .photoId(photoId)
                            .url(imageService.getPathByPhotoId(photoId))
                            .build());
            }
        }

    @Override
    public List<String> getProductPhotos(String productId) {
        Optional<List<ProductPhoto>> optionalProductPhotos =
                productPhotoRepository.findProductPhotosByProduct_ProductId(productId);
        if (optionalProductPhotos.isPresent()) {
            System.out.println(List.of(optionalProductPhotos.get().stream().map(ProductPhoto::getPhotoId)));
            return optionalProductPhotos.get().stream().map(ProductPhoto::getPhotoId).toList();
        }
        throw ProductNotFoundException.of(productId);
    }

    ///TODO
    @Override
    @Transactional
    public void deleteProductPhoto(DeleteProductPhotoRequest request) throws IOException {
//        Optional<List<ProductPhoto>> optionalProductPhotos =
//                productPhotoRepository.findAllByPhotoIdIn(request.photosId());
//        System.out.println(optionalProductPhotos.get().toString());
//        optionalProductPhotos.ifPresent(productPhotoRepository::deleteAll);
//        imageService.deletePhoto(request);
    }
}
