package com.scrumdogs.superprice.order.controllers;

public class OrderNotCreatedException extends RuntimeException{
    public static final String ERR_MSG = "Sorry, we're unable to process your order at this time.";

    public OrderNotCreatedException(String cause) {
        super(ERR_MSG + cause);
    }
}
