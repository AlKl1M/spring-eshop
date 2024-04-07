package com.bfu.cartservice.client;

import com.bfu.cartservice.controller.payload.CartProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="catalogue-service")
public interface CatalogueServiceClient {

    @GetMapping("/api/catalogue/{productId}")
    CartProductResponse getProductById(@PathVariable String productId);
}
