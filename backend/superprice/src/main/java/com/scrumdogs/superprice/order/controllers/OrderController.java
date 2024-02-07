package com.scrumdogs.superprice.order.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.scrumdogs.superprice.order.Order;
import com.scrumdogs.superprice.order.OrderService;
import com.scrumdogs.superprice.order.cart.OrderItem;

import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CrossOrigin
@RequiredArgsConstructor // This also performs @Autowiring if only one constructor
@RestController
public class OrderController {
    final private OrderService orderService;

    @PostMapping("v1/cart/checkout")
    @ResponseBody
    public ResponseEntity<Order> checkout(@RequestBody ObjectNode root) {

        //Convert the request body from JSON to POJO fields
        String name = root.get("name").asText();
        String email = root.get("email").asText();
        String phone = root.get("phone").asText();
        String address = root.get("address").asText();
        String postcode = root.get("postcode").asText();
        String deliveryDate = root.get("deliveryDate").asText();
        String deliveryWindowStart = root.get("deliveryWindowStart").asText();
        String customMessage = root.get("customMessage").asText();

        Iterator<JsonNode> iterator = root.get("items").elements();
        List<OrderItem> items = new ArrayList<>();

        while (iterator.hasNext()) {
            JsonNode jsonNode = iterator.next();
            items.add(new OrderItem(jsonNode.get("inventoryID").asLong(), jsonNode.get("quantity").asInt()));
        }

        //Send to the order service for validation against business rules and entry to the DB.
        return new ResponseEntity<>(
                orderService.createOrder(name, email, phone, address, postcode,
                                        deliveryDate, deliveryWindowStart, customMessage, items),
                HttpStatus.CREATED);
    }
}
