package com.scrumdogs.superprice.order.customer;

public class InvalidCustomerDetailsException extends RuntimeException {
    public InvalidCustomerDetailsException(String detail, String value, String help) {
        super("Invalid " + detail + ": " + value + ". " + help);
    }
}