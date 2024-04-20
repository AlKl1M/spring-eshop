package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.controller.payload.ProductReviewDto;
import com.bfu.feedbackservice.entity.ProductReview;

import java.util.List;

public interface ProductReviewsService {
    ProductReview createProductReview(String productId, int rating, String review, String username, String userId);
    List<ProductReviewDto> findProductReviewsByProduct(String productId);
    List<ProductReviewDto> findLatestProductReviews();
    void updateProductReview(String productId, int rating, String review, String userId);
    void deleteProductReview(String productId, String userId);
}
