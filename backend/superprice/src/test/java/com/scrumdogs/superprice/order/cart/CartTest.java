package com.scrumdogs.superprice.order.cart;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CartTest {

    //List of order items - each test will add items for its own test case.
    private final List<OrderItem> items = new ArrayList<>();

    @BeforeEach
    public void createValidItemsList() {
        //Create a list of valid order items as a base case.
        this.items.add(new OrderItem(1,3));
    }

    @Test
    public void shouldCreateCartWithZeroTotal_whenValidListOfItems() {
        Cart cart = new Cart(this.items);

        assertEquals(cart.getTotalInCents(), 0);
        assertEquals(cart.getItems().get(0), this.items.get(0));
    }

    @Test
    public void shouldThrowException_whenItemHasZeroInventoryID() {
        //Invalidate the cart by adding an order item with an inventory ID of 0.
        this.items.add(new OrderItem(0, 3));

        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () -> {
            Cart cart = new Cart(this.items);
            System.out.println(cart);
        });

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.INV_ID_ERR_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenItemHasNegativeInventoryID() {
        //Invalidate the cart by adding an order item with a negative inventory ID.
        this.items.add(new OrderItem(-1, 3));

        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () -> {
            Cart cart = new Cart(this.items);
            System.out.println(cart);
        });

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.INV_ID_ERR_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenItemHasZeroQuantity() {
        //Invalidate the cart by adding an order item with a quantity of 0.
        this.items.add(new OrderItem(1, 0));

        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () -> {
            Cart cart = new Cart(this.items);
            System.out.println(cart);
        });

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.QTY_ERR_MSG +
                this.items.get(this.items.size() - 1).getQuantity();
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenItemHasNegativeQuantity() {
        //Invalidate the cart by adding an order item with a negative quantity.
        this.items.add(new OrderItem(1, -3));

        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () -> {
            Cart cart = new Cart(this.items);
            System.out.println(cart);
        });

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.QTY_ERR_MSG +
                this.items.get(this.items.size() - 1).getQuantity();
        assertEquals(ex.getMessage(), expected);
    }
}
