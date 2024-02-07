package com.scrumdogs.superprice.category.controllers;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Could not find category with id " + id);
    }
}