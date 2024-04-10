package com.bfu.cartservice.client;

import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Component
public class CartServiceClientImpl implements CartServiceClient{
    private WebClient webClient;

    @Override
    public SimplifiedProductResponse getProductInfo(String productId) {
        String uri = "http://localhost:8081/api/catalogue/simplified-product-info/" + productId;
        SimplifiedProductResponse cartProductResponse = webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(SimplifiedProductResponse.class)
                .block();
        return cartProductResponse;
    }
}
