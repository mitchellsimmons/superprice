package com.scrumdogs.superprice.order.controllers;

public class EmailNotSentException extends RuntimeException {
    public static final String EMAIL_ERROR_MSG = """
                                Super whoops! Unable to email your order to delivery partner. :(
                                Please contact SuperPrice admin for details - we apologise for any inconvenience caused.
                                 ERROR_CAUSE =\s""";

    public EmailNotSentException(String cause) {
        super(EMAIL_ERROR_MSG + cause);
    }
}
