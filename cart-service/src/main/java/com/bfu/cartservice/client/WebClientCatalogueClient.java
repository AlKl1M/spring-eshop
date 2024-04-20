package com.bfu.cartservice.client;

import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class WebClientCatalogueClient implements CatalogueClient{
    private final WebClient webClient;

    @Override
    public SimplifiedProductResponse getProductInfo(String productId) {
        return webClient
                .get()
                .uri("/api/catalogue/simplified-product-info?productId={productId}", productId)
                .retrieve()
                .bodyToMono(SimplifiedProductResponse.class)
                .block();
    }
}