package com.bfu.feedbackservice.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewFavouriteProductPayload(
        @NotNull
        String productId
) {
}
