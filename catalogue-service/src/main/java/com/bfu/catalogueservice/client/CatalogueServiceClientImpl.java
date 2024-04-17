package com.bfu.catalogueservice.client;

import com.bfu.catalogueservice.controller.payload.Product.ArrayOfProductsIdRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@AllArgsConstructor
@Component
public class CatalogueServiceClientImpl implements CatalogueServiceClient{
    private final WebClient webClient;
    @Override
    public ArrayOfProductsIdRequest getProductsIdByUserId() {
        String uri = "http://localhost:8084/api/favourite-products/productsId";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ArrayOfProductsIdRequest.class)
                .block();
    }
}
