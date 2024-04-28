package com.bfu.catalogueservice.service.product_photo;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.entity.ProductPhoto;
import com.bfu.catalogueservice.exception.ProductNotFoundException;
import com.bfu.catalogueservice.repository.ProductPhotoRepository;
import com.bfu.catalogueservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductPhotoServiceImpl implements ProductPhotoService{
    private final ProductPhotoRepository productPhotoRepository;
    private final String GLOBAL_PATH = Paths.get("").toAbsolutePath().toString();
    @Override
    public void createProductPhoto(CreateProductPhotoResponse productPhotoResponse) throws IOException {
        boolean flag = true;
        for (BufferedImage bufferedImage: productPhotoResponse.images()){
            String photoId = String.format("%016d", new BigInteger(UUID.randomUUID().toString()
                    .replace("-", "").substring(0,15), 16));
            String path =
                    GLOBAL_PATH + String.format("/catalogue-service/src/main/resources/products-photo/%s/%s.jpg",
                            productPhotoResponse.product().getProductId(),
                            photoId
                            );
            File outputfile = new File(path);
            System.out.println(outputfile.getAbsolutePath());
            outputfile.mkdirs();
            ImageIO.write(bufferedImage, "jpg", outputfile);
            if (flag) {
                productPhotoRepository.save(
                        ProductPhoto.builder()
                                .isPreview(flag)
                                .product(productPhotoResponse.product())
                                .photoId(photoId)
                                .url(outputfile.getAbsolutePath())
                                .build()
                );
                flag = false;
            }
            else {
                productPhotoRepository.save(
                    ProductPhoto.builder()
                            .isPreview(flag)
                            .product(productPhotoResponse.product())
                            .photoId(photoId)
                            .url(outputfile.getAbsolutePath())
                            .build());
            }
        }
    }
}
