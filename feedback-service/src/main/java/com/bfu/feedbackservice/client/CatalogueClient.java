package com.bfu.feedbackservice.client;

import com.bfu.feedbackservice.controller.payload.SimplifiedProductResponse;

public interface CatalogueClient {
    Boolean isProductExist(String productId);
}