package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.customer.CustomerDetails;

import lombok.Getter;
import lombok.Setter;


@Getter
public class Order {
    @Setter private long id;
    private final CustomerDetails details;
    private final Cart cart;
    @Setter private OrderStatus status;

    public final static long UNSET = -1;

/*
    public Order() {
        //All testing data - feel free to add your own
        this.id = 1;
        this.details = new CustomerDetails("Scrumdad Millionarius",
                                            "big.papi.scrum03@gmail.com",
                                            "61372786323",
                                            "3/30 Scrumdog Lane",
                                            "3003",
                                            "2023-12-31",
                                            "16:00",
                                            "Just yeet it in the general vicinity of the property - aka Amazon S.O.P.");

        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(1, 12));
        items.add(new OrderItem(33, 1));
        items.add(new OrderItem(18, 19));
        items.add(new OrderItem(56, 3));
        items.add(new OrderItem(6, 9));
        this.cart = new Cart(items);

        this.status = OrderStatus.EMAILED;    //Change this, depending on what you want to test
    }
*/

    public Order(CustomerDetails details, Cart cart) {
        this.id = UNSET;
        this.details = details;
        this.cart = cart;
        this.status = OrderStatus.SUBMITTED;
    }

    public void setCartTotal(int totalInCents) {
        this.cart.setTotalInCents(totalInCents);
    }
}