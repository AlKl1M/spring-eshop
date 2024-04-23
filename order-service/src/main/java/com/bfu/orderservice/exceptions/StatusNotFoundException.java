package com.bfu.orderservice.exceptions;

public class StatusNotFoundException extends RuntimeException{
    public StatusNotFoundException(String status){
        super(String.format("Status with name=%d not found", status));
    }

    public static StatusNotFoundException of(String status){
        return new StatusNotFoundException(status);
    }
}
