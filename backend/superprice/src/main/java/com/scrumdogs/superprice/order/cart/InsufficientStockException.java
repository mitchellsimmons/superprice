package com.scrumdogs.superprice.order.cart;

public class InsufficientStockException extends RuntimeException {
    //Error messages
    public static final String ERR_MSG_START = "Sorry, we're unable to add all the selected items to your Order. Item: ";
    public static final String ERR_MSG_MIDDLE = " stock is too low. There's only: ";
    public static final String ERR_MSG_END = " left! You ordered: ";

    public InsufficientStockException(long inventoryID, int stockLevel, int quantity) {
        super(ERR_MSG_START + inventoryID + ERR_MSG_MIDDLE + stockLevel + ERR_MSG_END + quantity);
    }
}
