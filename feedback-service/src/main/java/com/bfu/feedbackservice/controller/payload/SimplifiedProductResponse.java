package com.bfu.feedbackservice.controller.payload;

import java.math.BigDecimal;

public record SimplifiedProductResponse(
        String productId,
        String name,
        BigDecimal price
) {
}
