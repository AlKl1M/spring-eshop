package com.bfu.catalogueservice.exception;

public class CanNotReadPhotoException extends RuntimeException{
    public CanNotReadPhotoException(byte [] imageBytes){
        super(String.format("This mas of byte=%d is not valid", imageBytes.toString()));
    }

    public static CanNotReadPhotoException of(byte[] imageBytes){
        return new CanNotReadPhotoException(imageBytes);
    }
}
