package com.scrumdogs.superprice.product.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ProductNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)
    ResponseStatusException handleProductNotFound(ProductNotFoundException ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }
}
