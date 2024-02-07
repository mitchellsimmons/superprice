package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.controllers.OrderController;
import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.cart.OrderItem;
import com.scrumdogs.superprice.order.customer.CustomerDetails;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class OrderControllerTest {

    private OrderController controller;

    private OrderService service;

    //Details to be shared by all tests.
    private String name;
    private String email;
    private String phone;
    private String address;
    private String postcode;
    private String deliveryDate;
    private String deliveryWindowStart;
    private String customMessage;

    //List of order items - each test will add items for its own test case.
    private final List<OrderItem> items = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectNode root = objectMapper.createObjectNode();
    private final ArrayNode itemsNode = objectMapper.createArrayNode();

    @BeforeEach
    void setup() {
        this.service = mock(OrderService.class);
        this.controller = new OrderController(this.service);

        //These are all valid details, the tests below will selectively invalidate one of them to run the test
        this.name = "Scrumdad Millionarius";
        this.email = "big.papi.scrum03@gmail.com";
        this.phone = "61372786323";
        this.address = "3/30 Scrumdog Lane";
        this.postcode = "3003";
        this.deliveryDate = "3023-12-31";
        this.deliveryWindowStart = "16:00";
        this.customMessage = "Just yeet it in the general vicinity of the property - aka Amazon S.O.P.";

        //Create a list of valid order items as a base case.
        this.items.add(new OrderItem(1,13));
        this.items.add(new OrderItem(33,1));
        this.items.add(new OrderItem(18,19));
        this.items.add(new OrderItem(56,3));
        this.items.add(new OrderItem(6,9));
    }

    @Test
    public void shouldReturnOK_whenValidInformationGiven() {
        // Add the request body fields as ObjectNodes
        root.put("name", name);
        root.put("email", email);
        root.put("phone", phone);
        root.put("address", address);
        root.put("postcode", postcode);
        root.put("deliveryDate", deliveryDate);
        root.put("deliveryWindowStart", deliveryWindowStart);
        root.put("customMessage", customMessage);

        for (OrderItem item : items) {
            ObjectNode itemNode = objectMapper.createObjectNode();
            itemNode.put("inventoryID", item.getInventoryID());
            itemNode.put("quantity", item.getQuantity());
            itemsNode.add(itemNode);
        }
        root.set("items", itemsNode);

        CustomerDetails details = new CustomerDetails(name, email, phone, address, postcode,
                                                        deliveryDate, deliveryWindowStart, customMessage);
        Cart cart = new Cart(items);

        when(this.service.createOrder(name, email, phone, address, postcode,
                                        deliveryDate, deliveryWindowStart, customMessage, items))
                                        .thenReturn(new Order(details, cart));

        ResponseEntity<Order> result = this.controller.checkout(root);
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
    }
}

