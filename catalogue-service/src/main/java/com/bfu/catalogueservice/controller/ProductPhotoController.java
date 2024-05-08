package com.bfu.catalogueservice.controller;

import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.DeleteProductPhotoRequest;
import com.bfu.catalogueservice.service.product_photo.ProductPhotoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.servlet.function.ServerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;


@RestController
@RequestMapping("/api/catalogue")
@RequiredArgsConstructor
public class ProductPhotoController {

    private final ProductPhotoService productPhotoService;
    private final ResourceLoader resourceLoader;

    @PostMapping("/create-product-photo")
    public ResponseEntity<?> createProductPhoto(@RequestBody CreateProductPhotoResponse response) throws IOException {
        productPhotoService.createProductPhoto(response);
        return ResponseEntity.ok("Photo has been created");
    }
    public List<String> getProductPhotos(String productId){
        return productPhotoService.getProductPhotos(productId);
    }
    @GetMapping("/get-product-photo")
    public void getProductPhoto(HttpServletResponse response, @RequestParam String productId, String photoId) throws IOException {
        Resource res = resourceLoader.getResource(String.format("classpath:products-photo/%s/Catalogue/%s.jpg",productId,photoId));
        InputStream inputStream = res.getInputStream();
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
    @DeleteMapping("/delete-catalogue-product-photo")
    public ResponseEntity<?> deleteCatalogueProductPhotos(@RequestBody DeleteProductPhotoRequest request) throws IOException {
        productPhotoService.deleteProductPhoto(request);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete-preview-product-photo")
    public ResponseEntity<?> deletePreviewProductPhoto(@RequestParam String productId) throws IOException {
        productPhotoService.deletePreviewProductPhoto(productId);
        return ResponseEntity.noContent().build();
    }

    public String getPreviewPhoto(String productId) {
        return productPhotoService.getPreviewPhoto(productId);
    }
}
