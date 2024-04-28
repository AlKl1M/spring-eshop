package com.bfu.catalogueservice.service.image;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.DeleteProductPhotoRequest;
import com.bfu.catalogueservice.entity.ValuePhoto;
import com.bfu.catalogueservice.exception.CanNotReadPhotoException;
import lombok.AllArgsConstructor;
import org.aspectj.util.FileUtil;
import org.flywaydb.core.internal.util.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{
    private final ResourceLoader resourceLoader;
    private final String GLOBAL_PATH = Paths.get("").toAbsolutePath().toString();

    @Override
    public List<BufferedImage> createBufferedImages(List<byte[]> images) {
        return images.stream().map(p-> {
            try {
                return ImageIO.read(new ByteArrayInputStream(p));
            } catch (IOException e) {
                throw CanNotReadPhotoException.of(p);
            }
        }).toList();
    }

    @Override
    public String createImage(BufferedImage bufferedImage, boolean flag, String productId) throws IOException {
        String photoId = String.format("%016d", new BigInteger(UUID.randomUUID().toString()
                .replace("-", "").substring(0,15), 16));
        String path = GLOBAL_PATH + String.format("/catalogue-service/src/main/resources/products-photo/%s/%s/%s.jpg",
                productId,
                ValuePhoto.checkValue(flag),
                photoId
        );
        File outputfile = new File(path);
        outputfile.mkdirs();
        ImageIO.write(bufferedImage, "jpg", outputfile);
        return photoId;
    }
    @Override
    public String getPathByPhotoId(String photoId) {
        return Paths.get(photoId).toAbsolutePath().toString();
    }

    @Override
    public void deletePhoto(DeleteProductPhotoRequest request) throws IOException {
        for (String photoId: request.photosId()) {
            Resource res = resourceLoader.getResource(String.format("classpath:products-photo/%s/Catalogue/%s.jpg",
                    request.productId(), photoId));
            File file = res.getFile();
            Files.delete(Path.of(file.getPath()));
        }
    }
}
