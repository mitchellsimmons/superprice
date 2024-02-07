package com.scrumdogs.superprice.order.cart;

public class InvalidOrderItemException extends RuntimeException {
    //Error messages
    public static final String BASE_ERR_MSG = "Sorry, we're unable to add all the selected items to your Order. Item: ";
    public static final String INV_ID_ERR_MSG = " does not exist.";
    public static final String QTY_ERR_MSG= " has invalid quantity: ";

    public InvalidOrderItemException(long inventoryID) {
        super(BASE_ERR_MSG + inventoryID + INV_ID_ERR_MSG);
    }

    public InvalidOrderItemException(long inventoryID, long quantity) {
        super(BASE_ERR_MSG + inventoryID + QTY_ERR_MSG + quantity);
    }
}
