package com.bfu.cartservice.client;

import com.bfu.cartservice.controller.payload.SimplifiedProductResponse;

public interface CartServiceClient {
    SimplifiedProductResponse getProductInfo(String productId);
}
