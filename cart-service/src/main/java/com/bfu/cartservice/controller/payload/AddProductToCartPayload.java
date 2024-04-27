package com.bfu.cartservice.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddProductToCartPayload(
        @NotNull(message="{cart.add.errors.product_id_is_null}")
        String productId,
        @Min(value = 1, message = "{cart.add.quantity_is_below_min}")
        Integer quantity
) {
}
