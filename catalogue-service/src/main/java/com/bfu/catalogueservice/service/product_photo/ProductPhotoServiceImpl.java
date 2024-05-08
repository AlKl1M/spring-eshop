package com.bfu.catalogueservice.service.product_photo;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.DeleteProductPhotoRequest;
import com.bfu.catalogueservice.entity.ProductPhoto;
import com.bfu.catalogueservice.exception.ProductNotFoundException;
import com.bfu.catalogueservice.repository.ProductPhotoRepository;
import com.bfu.catalogueservice.service.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

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
                                .preview(flag)
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
                            .preview(flag)
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

    @Override
    @Transactional
    public void deleteProductPhoto(DeleteProductPhotoRequest request) throws IOException {
        Optional<List<ProductPhoto>> optionalProductPhotos =
                productPhotoRepository.findProductPhotosByPhotoIdInAndProduct_ProductId(
                        request.photosId(), request.productId()
                );
        optionalProductPhotos.ifPresent(productPhotoRepository::deleteAll);
        for (String photoId: request.photosId()){
            imageService.deletePhoto(request.productId(), "Catalogue", photoId);
        }
    }

    @Override
    public String getPreviewPhoto(String productId) {
        Optional<ProductPhoto> optionalProduct =
                productPhotoRepository.findProductPhotoByPreviewTrueAndProduct_ProductId(productId);
        if (optionalProduct.isPresent())
            return optionalProduct.get().getPhotoId();
        throw ProductNotFoundException.of(productId);
    }

    @Override
    @Transactional
    public void deletePreviewProductPhoto(String productId) throws IOException {
        Optional<ProductPhoto> optionalProduct =
                productPhotoRepository.findProductPhotoByPreviewTrueAndProduct_ProductId(productId);
        if (optionalProduct.isPresent()){
            imageService.deletePhoto(productId, "Preview", optionalProduct.get().getPhotoId());
            productPhotoRepository.delete(optionalProduct.get());
        }
    }
}
