package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.InsufficientStockException;
import com.scrumdogs.superprice.order.cart.InvalidOrderItemException;
import com.scrumdogs.superprice.order.cart.OrderItem;
import com.scrumdogs.superprice.order.customer.InvalidCustomerDetailsException;

import java.util.List;

public interface OrderService {
    //Create an order using the given customer details and list of order items
    Order createOrder(String name, String email, String phone, String address, String postcode,
                      String deliveryDate, String deliveryWindowStart, String customMessage, List<OrderItem> items)
            throws InvalidOrderItemException, InvalidCustomerDetailsException, InsufficientStockException;
}
