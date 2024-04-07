package com.bfu.cartservice.controller.payload;

import java.math.BigDecimal;

public record CartProductResponse(String id,
                                  String name,
                                  BigDecimal price) {
}
