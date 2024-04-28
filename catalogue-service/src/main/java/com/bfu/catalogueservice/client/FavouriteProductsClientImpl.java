package com.bfu.catalogueservice.client;

import com.bfu.catalogueservice.controller.payload.Product.ArrayOfProductsIdRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class FavouriteProductsClientImpl implements FavouriteProductsClient{
    private final RestClient restClient;
    @Override
    public ArrayOfProductsIdRequest getProductsIdByUserId() {
        return this.restClient
                .get()
                .uri("http://localhost:8084/api/favourite-products/productsId")
                .retrieve()
                .body(ArrayOfProductsIdRequest.class);
    }
}
