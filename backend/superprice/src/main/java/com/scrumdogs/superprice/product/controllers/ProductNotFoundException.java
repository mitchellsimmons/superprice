package com.scrumdogs.superprice.product.controllers;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Could not find product with ID = " + id);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

}

