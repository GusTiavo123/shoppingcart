package com.gustavo.shoppingcart.exceptions.category;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException(String message) {
        super(message);
    }
}
