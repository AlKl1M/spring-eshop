package com.bfu.orderservice.exceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String userId){
        super(String.format("Order with userId=%d not found", userId));
    }
    public static OrderNotFoundException of(String userId){
        return new OrderNotFoundException(userId);
    }
}
