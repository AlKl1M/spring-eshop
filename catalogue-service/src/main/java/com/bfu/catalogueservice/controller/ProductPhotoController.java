package com.bfu.catalogueservice.controller;

import com.bfu.catalogueservice.service.product_photo.ProductPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogue")
@AllArgsConstructor
public class ProductPhotoController {

    private final ProductPhotoService productPhotoService;
//    @PostMapping("/create-product-photo")
//    public void createProductPhoto(@RequestBody CreateProductImages productImages){
//
//    }
}
