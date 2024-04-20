package com.bfu.feedbackservice.controller.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductReviewPayload(
        @NotNull(message = "feedback.review.create.errors.productid_is_null")
        String productId,
        @NotNull(message = "feedback.review.create.errors.rating_is_null")
        @Min(value = 1, message = "{feedback.review.create.errors.rating_is_below_min}")
        @Max(value = 5, message = "{feedback.review.create.errors.rating_is_above_max}")
        Integer rating,
        @Size(max = 1000, message = "feedback.review.create.errors.rewiew_size_if_invalid")
        String review
) {
}
