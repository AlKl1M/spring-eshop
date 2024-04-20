package com.bfu.feedbackservice.controller.payload;

import com.bfu.feedbackservice.entity.ProductReview;

import java.util.Date;
import java.util.UUID;

public record ProductReviewDto(
        UUID id,
        String productId,
        int rating,
        String review,
        Date currectDate,
        String username
) {
    public static ProductReviewDto from(ProductReview review) {
        return new ProductReviewDto(review.getId(), review.getProductId(), review.getRating(), review.getReview(), review.getCreatedDate(), review.getUsername());
    }
}
