package com.bfu.orderservice.controller.payload;

public record ChangeStatusRequest(
        String orderId,
        String status
) {
}
