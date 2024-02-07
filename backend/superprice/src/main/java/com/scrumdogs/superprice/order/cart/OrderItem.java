package com.scrumdogs.superprice.order.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderItem {
    private final long inventoryID;
    @Setter private String productName;
    @Setter private String storeName;
    private int itemPriceInCents;
    private final int quantity;
    @Setter private int subTotalInCents;

    public OrderItem(long inventoryID, int quantity) {
        this.inventoryID = inventoryID;
        this.quantity = quantity;
    }

    public void setItemPriceInCents(int itemPriceInCents) {
        this.itemPriceInCents = itemPriceInCents;

        //Also calculate the sub-total for this order item, once the item price is known.
        this.subTotalInCents = itemPriceInCents * this.quantity;
    }
}
