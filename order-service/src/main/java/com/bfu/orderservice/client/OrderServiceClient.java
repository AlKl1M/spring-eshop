package com.bfu.orderservice.client;

import com.bfu.orderservice.controller.payload.ArrayOfSimplifiedProduct;

import java.util.List;

public interface OrderServiceClient {
    ArrayOfSimplifiedProduct getCart();
    List<String> getProductsId();

}
