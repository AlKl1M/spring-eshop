package com.bfu.orderservice.exceptions;

import java.util.List;

public class OrderProductsNotFoundException extends RuntimeException{
    public OrderProductsNotFoundException(String orderId){
        super(String.format("OrderProducts not found with orderId=%d", orderId));
    }
    public static OrderProductsNotFoundException of(String orderId){
        return new OrderProductsNotFoundException(orderId);
    }
}
