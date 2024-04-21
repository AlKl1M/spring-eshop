package com.bfu.orderservice.client;

import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderServiceClientImpl implements OrderServiceClient{
    private WebClient webClient;

    @Override
    public ArrayOfSimplifiedProduct getCart() {
        String uri = "http://localhost:8083/api/cart/";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ArrayOfSimplifiedProduct.class)
                .block();
    }

    @Override
    public List<String> getProductsId() {
        return null;
    }
}
