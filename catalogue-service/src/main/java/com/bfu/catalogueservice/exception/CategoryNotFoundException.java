package com.bfu.catalogueservice.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String categoryId){
        super(String.format("Category with id=%d not found", categoryId));
    }

    public static CategoryNotFoundException of(String categoryId) {
        return new CategoryNotFoundException(categoryId);
    }
}
