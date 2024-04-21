package com.bfu.cartservice.client;

import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;

public interface CatalogueClient {
    SimplifiedProductResponse getProductInfo(String productId);
}