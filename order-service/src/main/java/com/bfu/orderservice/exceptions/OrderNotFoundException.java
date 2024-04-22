package com.bfu.orderservice.exceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String bodyException){
        super(bodyException);
    }
    public static OrderNotFoundException ofUser(String userId){
        return new OrderNotFoundException(String.format("Order with userId=%d not found", userId));
    }
    public static OrderNotFoundException ofOrder(String orderId){
        return new OrderNotFoundException(String.format("Order with orderId=%d not found", orderId));
    }
}
