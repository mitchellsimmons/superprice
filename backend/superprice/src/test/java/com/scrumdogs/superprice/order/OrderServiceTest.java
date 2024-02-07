package com.scrumdogs.superprice.order;

import com.scrumdogs.superprice.order.cart.Cart;
import com.scrumdogs.superprice.order.cart.InvalidOrderItemException;
import com.scrumdogs.superprice.order.cart.OrderItem;
import com.scrumdogs.superprice.order.customer.CustomerDetails;
import com.scrumdogs.superprice.order.customer.InvalidCustomerDetailsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    private OrderService service;
    private final OrderRepository orderRepositoryMock = mock(OrderRepository.class);

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
    void setup() {
        this.service = new OrderServiceImpl(orderRepositoryMock);

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
        this.expectedTotalInCents = 4070;

        when(orderRepositoryMock.insertCustomerDetails(any())).thenReturn(1L);
        when(orderRepositoryMock.calculateTotal(any())).thenReturn(expectedTotalInCents);
    }

    @Test
    public void shouldCreateApprovedOrder_whenValidDetailsAndItemsGiven() {
        //Keep the valid data for this test.
        CustomerDetails testDetails = new CustomerDetails(name, email, phone, address, postcode,
                                                            deliveryDate, deliveryWindowStart, customMessage);
        Cart testCart = new Cart(items);
        testCart.setTotalInCents(expectedTotalInCents);

        Order result = service.createOrder(name, email, phone, address, postcode,
                                            deliveryDate, deliveryWindowStart, customMessage, items);

        assertEquals(result.getId(), 1L);
        assertEquals(result.getDetails().getName(), testDetails.getName());
        assertEquals(result.getDetails().getEmail(), testDetails.getEmail());
        assertEquals(result.getDetails().getPhone(), testDetails.getPhone());
        assertEquals(result.getDetails().getAddress(), testDetails.getAddress());
        assertEquals(result.getDetails().getPostcode(), testDetails.getPostcode());
        assertEquals(result.getDetails().getDeliveryDate(), testDetails.getDeliveryDate());
        assertEquals(result.getDetails().getDeliveryWindowStart(), testDetails.getDeliveryWindowStart());
        assertEquals(result.getDetails().getCustomMessage(), testDetails.getCustomMessage());
        assertEquals(result.getCart().getItems(), testCart.getItems());
        assertEquals(result.getCart().getTotalInCents(), testCart.getTotalInCents());
        assertEquals(result.getStatus(), OrderStatus.EMAILED);
    }

    @Test
    public void shouldThrowException_inService_whenNameIsEmpty() {
        //Invalidate name by making it empty.
        this.name = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
                    service.createOrder(name, email, phone, address, postcode,
                                        deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenNameHasIllegalCharacter() {
        //Invalidate name by adding an illegal character.
        this.name += "!";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenNameTooLong() {
        //Invalidate name by making it too long.
        this.name += "a".repeat(CustomerDetails.MAX_NAME_LEN - name.length() + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                   deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "name" + ": " + CustomerDetails.trimLongString(name) + ". " +
                            CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenOnlyOneNameProvided() {
        //Invalidate name by only giving first name.
        this.name = "Scrumdad";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmptyEmail() {
        //Invalidate email by making it empty.
        this.email = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "email" + ": " + email + ". " + CustomerDetails.EMAIL_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenInvalidEmail() {
        //Invalidate email by adding illegal character.
        this.email = "#" + email;

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "email" + ": " + email + ". " + CustomerDetails.EMAIL_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmailTooLong() {
        //Invalidate email by making it too long.
        this.email += "a".repeat(CustomerDetails.MAX_EMAIL_LEN - email.length() + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "email" + ": " + CustomerDetails.trimLongString(email) + ". "
                            + CustomerDetails.EMAIL_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmptyPhone() {
        //Invalidate phone by making it empty.
        this.phone = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "phone" + ": " + phone + ". " + CustomerDetails.PHONE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenInvalidPhone() {
        //Invalidate phone by adding illegal characters.
        this.phone = "ph" + phone;

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "phone" + ": " + phone + ". " + CustomerDetails.PHONE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmptyAddress() {
        //Invalidate address by making it empty.
        this.address = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "address" + ": " + address + ". " + CustomerDetails.ADDRESS_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenInvalidAddress() {
        //Invalidate address by formatting it badly.
        this.address = "Unit 1 123 Main Street";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "address" + ": " + address + ". " + CustomerDetails.ADDRESS_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenAddressHasInvalidCharacter() {
        //Invalidate address by adding an invalid character.
        this.address = "1@123 Main Street";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "address" + ": " + address + ". " + CustomerDetails.ADDRESS_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmptyPostcode() {
        //Invalidate postcode by making it empty.
        this.postcode = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenPostcodeTooLong() {
        //Invalidate postcode by exceeding maximum length.
        this.postcode += "0".repeat(CustomerDetails.REQ_POSTCODE_LEN - this.postcode.length() + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                    deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenInvalidPostcodeCharacter() {
        //Invalidate postcode by adding illegal character.
        this.postcode = this.postcode.replace("0", "O");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenPostcodeOutOfRange_tooLow() {
        //Invalidate postcode by putting it outside range.
        this.postcode = String.valueOf(CustomerDetails.POSTCODE_MIN - 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenPostcodeOutOfRange_tooHigh() {
        //Invalidate postcode by putting it outside range.
        this.postcode = String.valueOf(CustomerDetails.POSTCODE_MAX + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                    deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmptyDeliveryDate() {
        //Invalidate delivery by making it empty.
        this.deliveryDate = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryDateHasIllegalCharacter() {
        //Invalidate delivery date by adding illegal character.
        this.deliveryDate = this.deliveryDate.replace("0", "O");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryDatePoorlyFormatted() {
        //Invalidate delivery date by removing - separator.
        this.deliveryDate = this.deliveryDate.replace("-", "");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryDateInPast() {
        //Invalidate delivery date by making it in the past.
        this.deliveryDate = this.deliveryDate.replace("3023", "1989");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_IN_PAST_HELP_MSG + CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenEmptyDeliveryWindow() {
        //Invalidate delivery by making it empty.
        this.deliveryWindowStart = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryWindowHasIllegalCharacter() {
        //Invalidate delivery window by adding illegal character.
        this.deliveryWindowStart = this.deliveryWindowStart.replace("0", "O");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryWindowPoorlyFormatted() {
        //Invalidate delivery window by removing HH:MM separator.
        this.deliveryWindowStart = this.deliveryWindowStart.replace(":", "");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryWindowOutOfRange() {
        //Invalidate delivery window by putting it outside range.
        this.deliveryWindowStart = this.deliveryWindowStart.replace("6", "9");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryWindowOutside24H_format() {
        //Invalidate delivery window by putting it outside 24H format.
        this.deliveryWindowStart = "25:00";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenDeliveryWindowHasMinutes() {
        //Invalidate delivery window by adding an offset from the hour.
        this.deliveryWindowStart = "20:01";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenItemHasZeroInventoryID() {
        //Invalidate the cart by adding an order item with an inventory ID of 0.
        this.items.add(new OrderItem(0, 3));

        @SuppressWarnings("unused")
        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.INV_ID_ERR_MSG;
        assertEquals(ex.getMessage(), expected);
    }
    
    @Test
    public void shouldThrowException_inService_whenItemHasNegativeInventoryID() {
        //Invalidate the cart by adding an order item with a negative inventory ID.
        this.items.add(new OrderItem(-1, 3));

        @SuppressWarnings("unused")
        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.INV_ID_ERR_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenItemHasZeroQuantity() {
        //Invalidate the cart by adding an order item with a quantity of 0.
        this.items.add(new OrderItem(1, 0));

        @SuppressWarnings("unused")
        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.QTY_ERR_MSG +
                this.items.get(this.items.size() - 1).getQuantity();
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_inService_whenItemHasNegativeQuantity() {
        //Invalidate the cart by adding an order item with a negative quantity.
        this.items.add(new OrderItem(1, -3));

        @SuppressWarnings("unused")
        InvalidOrderItemException ex = assertThrows(InvalidOrderItemException.class, () ->
            service.createOrder(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage, items));

        String expected = InvalidOrderItemException.BASE_ERR_MSG +
                this.items.get(this.items.size() - 1).getInventoryID() +
                InvalidOrderItemException.QTY_ERR_MSG +
                this.items.get(this.items.size() - 1).getQuantity();
        assertEquals(ex.getMessage(), expected);
    }
}
