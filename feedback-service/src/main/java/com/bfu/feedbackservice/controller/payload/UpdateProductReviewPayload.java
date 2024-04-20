package com.bfu.feedbackservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductReviewPayload(
        @NotNull(message = "feedback.review.update.errors.productid_is_null")
        String productId,
        @NotNull(message = "feedback.review.update.errors.rating_is_null")
        @Size(min = 1, max=5, message = "feedback.review.update.errors.rating_size_is_invalid")
        Integer rating,
        @Size(max = 1000, message = "feedback.review.update.errors.rewiew_size_if_invalid")
        String review
) {
}
