package com.scrumdogs.superprice.order.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Cart {
    public static final String EMPTY_CART_MSG = "Your cart is empty!";

    private final List<OrderItem> items;    //Do not allow cart items to be updated after cart creation
    @Setter private int totalInCents;

    public Cart(List<OrderItem> items) {
        //Ensure the items are all well-formatted before adding them to the cart.
        for(OrderItem item : items) {
            if (item.getInventoryID() <= 0) {
                throw new InvalidOrderItemException(item.getInventoryID());
            }

            if (item.getQuantity() <= 0) {
                throw new InvalidOrderItemException(item.getInventoryID(), item.getQuantity());
            }
        }

        this.items = items;
        this.totalInCents = 0;
    }

    @Override
    public String toString() {
        StringBuilder cartString = new StringBuilder();

        if(this.items.isEmpty()) {
            cartString = new StringBuilder(EMPTY_CART_MSG);

        } else {
            for (OrderItem item : this.items) {
                double itemPriceInDollars = item.getItemPriceInCents() / 100.0;
                double lineTotalInDollars = itemPriceInDollars * item.getQuantity();

                cartString.append(String.format("\t*\t%-4d\tx\t%-40s\t-\t$%-7.2f\t@\t%15s\t:\t$%8.2f\n",
                        item.getQuantity(), item.getProductName(), itemPriceInDollars,
                        item.getStoreName(), lineTotalInDollars));

            }
        }

        return cartString.toString();
    }
}
