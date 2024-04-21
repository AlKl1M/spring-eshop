package com.bfu.feedbackservice.client;

import com.bfu.feedbackservice.controller.payload.SimplifiedProductResponse;

public interface CatalogueClient {
    SimplifiedProductResponse getProductInfo(String productId);
}