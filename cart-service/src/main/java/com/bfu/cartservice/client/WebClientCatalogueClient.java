package com.bfu.cartservice.client;

import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class WebClientCatalogueClient implements CatalogueClient{
    private final RestClient restClient;

    @Override
    public SimplifiedProductResponse getProductInfo(String productId) {
        return this.restClient
                .get()
                .uri("/api/catalogue/simplified-product-info?productId={productId}", productId)
                .retrieve()
                .body(SimplifiedProductResponse.class);
    }
}