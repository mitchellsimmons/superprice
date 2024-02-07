package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.cart.InsufficientStockException;
import com.scrumdogs.superprice.order.cart.OrderItem;
import com.scrumdogs.superprice.order.controllers.OrderNotCreatedException;

import com.scrumdogs.superprice.order.customer.CustomerDetails;
import org.flywaydb.core.Flyway;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private final OrderRepository repository = new OrderRepositoryImpl(dataSource);

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
    private int expectedTotalInCents;
    
    @BeforeEach
    public void setup() {
        //These are all valid details, the tests below will selectively invalidate one of them to run the test
        this.name = "Scrumdad Millionarius";
        this.email = "big.papi.scrum03@gmail.com";
        this.phone = "61372786323";
        this.address = "3/30 Scrumdog Lane";
        this.postcode = "3003";
        this.deliveryDate = "2023-12-31";
        this.deliveryWindowStart = "16:00";
        this.customMessage = "Just yeet it in the general vicinity of the property - aka Amazon S.O.P.";

        //Create a list of valid order items as a base case.
        this.items.add(new OrderItem(1,13));
        this.items.add(new OrderItem(33,1));
        this.items.add(new OrderItem(18,19));
        this.items.add(new OrderItem(56,3));
        this.items.add(new OrderItem(6,9));
        this.expectedTotalInCents = 40740;
        
        flyway.migrate();
    }
    
    @Test
    public void shouldReturnOrderID_whenDetailsWellFormatted() {
        CustomerDetails details = new CustomerDetails(name, email, phone, address, postcode,
                                                        deliveryDate, deliveryWindowStart, customMessage);
        assertEquals(repository.insertCustomerDetails(details), 1L);
    }

    @Test
    public void shouldCalculateCartTotalAccurately_whenListWellFormatted() {
        Cart cart = new Cart(items);
        assertEquals(repository.calculateTotal(cart), expectedTotalInCents);
    }

    @Test
    public void shouldThrowException_inRepo_whenInventoryItemWellFormattedButDoesNotExist() {
        //Invalidate the cart by adding an order item which does not exist.
        this.items.add(new OrderItem(Integer.MAX_VALUE, 1));

        OrderNotCreatedException ex = assertThrows(OrderNotCreatedException.class, () ->
                repository.calculateTotal(new Cart(this.items))
        );

        String expectedPrefix = OrderNotCreatedException.ERR_MSG + OrderRepositoryImpl.UNKNOWN_CALC_TOTAL_ERROR_MSG;
        assert(ex.getMessage().startsWith(expectedPrefix));
    }

    @Test
    public void shouldThrowException_inRepo_whenInsufficientStock() {
        //Invalidate the cart by adding an order item in a ridiculous quantity.
        OrderItem ridic = new OrderItem(1, Integer.MAX_VALUE);
        this.items.add(ridic);

        InsufficientStockException ex = assertThrows(InsufficientStockException.class, () ->
                repository.calculateTotal(new Cart(this.items))
        );

        String expectedPrefix = InsufficientStockException.ERR_MSG_START + ridic.getInventoryID();
        assert(ex.getMessage().startsWith(expectedPrefix));
    }
    
    @AfterEach
    public void teardown() {
        flyway.clean();
    }

}
