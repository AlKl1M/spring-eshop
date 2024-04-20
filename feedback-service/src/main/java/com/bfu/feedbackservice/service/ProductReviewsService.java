package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.entity.ProductReview;

import java.util.List;

public interface ProductReviewsService {
    ProductReview createProductReview(String productId, int rating, String review, String userId);
    List<ProductReview> findProductReviewsByProduct(String productId);
    void updateProductReview(String productId, int rating, String review, String userId);
    void deleteProductReview(String productId, String userId);
}
