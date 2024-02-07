package com.scrumdogs.superprice.order.controllers;

import com.scrumdogs.superprice.order.cart.InsufficientStockException;
import com.scrumdogs.superprice.order.customer.InvalidCustomerDetailsException;
import com.scrumdogs.superprice.order.cart.InvalidOrderItemException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class OrderNotCreatedAdvice {

    @ResponseBody
    @ExceptionHandler(OrderNotCreatedException.class)
    ResponseStatusException handleOrderNotCreated(OrderNotCreatedException ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
    }

    @ResponseBody
    @ExceptionHandler(InvalidCustomerDetailsException.class)
    ResponseStatusException handleInvalidCustomerDetails(InvalidCustomerDetailsException ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
    }

    @ResponseBody
    @ExceptionHandler(InvalidOrderItemException.class)
    ResponseStatusException handleInvalidOrderItem(InvalidOrderItemException ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
    }

    @ResponseBody
    @ExceptionHandler(InsufficientStockException.class)
    ResponseStatusException handleInsufficientStock(InsufficientStockException ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
    }

    @ResponseBody
    @ExceptionHandler(EmailNotSentException.class)
    ResponseStatusException handleEmailNotSent(EmailNotSentException ex) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        //https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500
    }
}

