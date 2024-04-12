package com.bfu.feedbackservice.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewProductReviewPayload(
        @NotNull()
        String productId,
        Integer rating,
        String review
) {
}
