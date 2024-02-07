package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.customer.CustomerDetails;

public interface OrderRepository {
    //Insert the given customer details into the DB and return the PK of the entry as the order id.
    long insertCustomerDetails(CustomerDetails details);

    //Calculate the total for the given cart of order items and return it.
    int calculateTotal(Cart cart);

    //Complete the oder process for a given order - decrementing stock, and marking as 'APPROVED' if successful.
    void checkout(Order order);

    //Simulate sending the order out to the delivery partner - this will also update the order status in DB as 'EMAILED'
    void sendEmail(Order order);
}
