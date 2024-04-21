package com.bfu.orderservice.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ArrayOfSimplifiedProduct(
        @NotNull
        @NotBlank
        @Min(1)
        List<SimplifiedProductResponse> products
) {
}
