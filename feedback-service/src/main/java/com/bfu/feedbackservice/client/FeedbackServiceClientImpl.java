package com.bfu.feedbackservice.client;

import com.bfu.feedbackservice.controller.payload.SimplifiedProductResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FeedbackServiceClientImpl implements FeedbackServiceClient{
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
