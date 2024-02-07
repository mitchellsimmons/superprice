package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.cart.InsufficientStockException;
import com.scrumdogs.superprice.order.cart.InvalidOrderItemException;
import com.scrumdogs.superprice.order.controllers.EmailNotSentException;
import com.scrumdogs.superprice.order.customer.CustomerDetails;
import com.scrumdogs.superprice.order.customer.InvalidCustomerDetailsException;
import com.scrumdogs.superprice.order.cart.OrderItem;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // Performs @Autowiring if only one constructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    @Override
    public Order createOrder(String name, String email, String phone, String address, String postcode,
                             String deliveryDate, String deliveryWindowStart, String customMessage,
                             List<OrderItem> items)

            throws InvalidCustomerDetailsException,
                    InvalidOrderItemException,
                    InsufficientStockException,
                    EmailNotSentException {

        //Ensure all order details are well-formatted before creating the order.
        CustomerDetails details = new CustomerDetails(name, email, phone, address, postcode,
                                                        deliveryDate, deliveryWindowStart, customMessage);
        Cart cart = new Cart(items);

        //Create the order.
        Order order = new Order(details, cart);

        //Insert order into the DB: set the PK of the entry as order ID.
        order.setId(repository.insertCustomerDetails(order.getDetails()));

        //Calculate the total for this cart - also checks the items are in stock.
        order.setCartTotal(repository.calculateTotal(order.getCart()));

        order.setStatus(OrderStatus.APPROVED);

        //Add the items in the cart to the DB against the orderID and decrement stock levels accordingly.
        repository.checkout(order);

        //Simulate sending the order out to the delivery partner.
        repository.sendEmail(order);

        order.setStatus(OrderStatus.EMAILED); //TODO: Tests will fail if looking for 'APPROVED'

        return order;
    }
}
