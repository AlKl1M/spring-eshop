package com.bfu.catalogueservice.client;

import com.bfu.catalogueservice.controller.payload.Product.ArrayOfProductsIdRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface FavouriteProductsClient {
    ArrayOfProductsIdRequest getProductsIdByUserId();
}
