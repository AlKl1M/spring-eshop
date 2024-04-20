package com.bfu.cartservice.controller.payload;

import java.util.List;

public record ArrayOfSimplifiedProduct(
        List<SimplifiedProductResponse> products
) {
}
