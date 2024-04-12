package com.bfu.feedbackservice.client;

import com.bfu.feedbackservice.controller.payload.SimplifiedProductResponse;

public interface FeedbackServiceClient {
    SimplifiedProductResponse getProductInfo(String productId);
}
