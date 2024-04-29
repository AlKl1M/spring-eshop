package com.bfu.feedbackservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class WebClientCatalogueClient implements CatalogueClient {
    private final RestClient restClient;

    @Override
    public Boolean isProductExist(String productId) {
        return this.restClient
                .get()
                .uri("/api/catalogue/is-exists?productId={productId}", productId)
                .retrieve()
                .body(Boolean.class);
    }
}
