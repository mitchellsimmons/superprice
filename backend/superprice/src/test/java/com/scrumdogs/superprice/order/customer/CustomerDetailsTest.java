package com.scrumdogs.superprice.order.customer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerDetailsTest {

    //Details to be shared by all tests.
    private String name;
    private String email;
    private String phone;
    private String address;
    private String postcode;
    private String deliveryDate;
    private String deliveryWindowStart;
    private String customMessage;

    @BeforeEach
    public void createValidDetails() {
        //These are all valid details, the tests below will selectively invalidate one of them to run the test
        this.name = "Scrumdad Millionarius";
        this.email = "big.papi.scrum03@gmail.com";
        this.phone = "61372786323";
        this.address = "3/30 Scrumdog Lane";
        this.postcode = "3003";
        this.deliveryDate = "2023-12-31";
        this.deliveryWindowStart = "16:00";
        this.customMessage = "Just yeet it in the general vicinity of the property - aka Amazon S.O.P.";
    }

    @Test
    public void shouldCreateCustomerDetails_whenValidDetailsGiven() {
        //Keep the valid details and create an object as desired.
        CustomerDetails details = new CustomerDetails(name, email, phone, address, postcode,
                                                        deliveryDate, deliveryWindowStart, customMessage);
        assertEquals(details.getName(), name);
        assertEquals(details.getEmail(), email);
        assertEquals(details.getPhone(), phone);
        assertEquals(details.getAddress(), address);
        assertEquals(details.getPostcode(), postcode);
        assertEquals(details.getDeliveryDate(), deliveryDate);
        assertEquals(details.getDeliveryWindowStart(), deliveryWindowStart);
        assertEquals(details.getCustomMessage(), customMessage);
    }

    @Test
    public void shouldThrowException_whenEmptyName() {
        //Invalidate name by making it empty.
        this.name = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenNameHasIllegalCharacter() {
        //Invalidate name by adding an illegal character.
        this.name += "!";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenNameTooLong() {
        //Invalidate name by making it too long.
        this.name += "a".repeat(CustomerDetails.MAX_NAME_LEN - name.length() + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "name" + ": " + CustomerDetails.trimLongString(name) + ". " +
                            CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenOnlyOneNameProvided() {
        //Invalidate name by only giving first name.
        this.name = "Scrumdad";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmptyEmail() {
        //Invalidate email by making it empty.
        this.email = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "email" + ": " + email + ". " + CustomerDetails.EMAIL_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenInvalidEmail() {
        //Invalidate email by adding illegal character.
        this.email = "#" + email;

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "email" + ": " + email + ". " + CustomerDetails.EMAIL_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmailTooLong() {
        //Invalidate email by making it too long.
        this.email += "a".repeat(CustomerDetails.MAX_EMAIL_LEN - email.length() + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "email" + ": " + CustomerDetails.trimLongString(email) + ". " +
                            CustomerDetails.EMAIL_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmptyPhone() {
        //Invalidate phone by making it empty.
        this.phone = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "phone" + ": " + phone + ". " + CustomerDetails.PHONE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenInvalidPhone() {
        //Invalidate phone by adding illegal characters.
        this.phone = "ph" + phone;

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "phone" + ": " + phone + ". " + CustomerDetails.PHONE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmptyAddress() {
        //Invalidate address by making it empty.
        this.address = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "address" + ": " + address + ". " + CustomerDetails.ADDRESS_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenInvalidAddress() {
        //Invalidate address by formatting it badly.
        this.address = "Unit 1 123 Main Street";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "address" + ": " + address + ". " + CustomerDetails.ADDRESS_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenAddressHasInvalidCharacter() {
        //Invalidate address by adding an invalid character.
        this.address = "1@123 Main Street";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "address" + ": " + address + ". " + CustomerDetails.ADDRESS_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmptyPostcode() {
        //Invalidate postcode by making it empty.
        this.postcode = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenInvalidPostcode() {
        //Invalidate postcode by adding illegal character.
        this.postcode = this.postcode.replace("0", "O");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenPostcodeOutOfRange() {
        //Invalidate postcode by putting it outside range.
        this.postcode = String.valueOf(CustomerDetails.REQ_POSTCODE_LEN+1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "postcode" + ": " + postcode + ". " + CustomerDetails.POSTCODE_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmptyDeliveryDate() {
        //Invalidate delivery date by making it empty.
        this.deliveryDate = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryDateHasIllegalCharacter() {
        //Invalidate delivery date by adding illegal character.
        this.deliveryDate = this.deliveryDate.replace("0", "O");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryDatePoorlyFormatted() {
        //Invalidate delivery date by removing - separator.
        this.deliveryDate = this.deliveryDate.replace("-", "");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryDateInPast() {
        //Invalidate delivery date by making it in the past.
        this.deliveryDate = this.deliveryDate.replace("2023", "1989");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery date" + ": " + deliveryDate + ". " +
                            CustomerDetails.DEL_DATE_IN_PAST_HELP_MSG + CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenEmptyDeliveryWindow() {
        //Invalidate delivery by making it empty.
        this.deliveryWindowStart = "";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryWindowHasIllegalCharacter() {
        //Invalidate delivery window by adding illegal character.
        this.deliveryWindowStart = this.deliveryWindowStart.replace("0", "O");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryWindowPoorlyFormatted() {
        //Invalidate delivery window by removing HH:MM separator.
        this.deliveryWindowStart = this.deliveryWindowStart.replace(":", "");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryWindowOutOfRange() {
        //Invalidate delivery window by putting it outside range.
        this.deliveryWindowStart = this.deliveryWindowStart.replace("6", "9");

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryWindowOutside24H_format() {
        //Invalidate delivery window by putting it outside 24H format.
        this.deliveryWindowStart = "25:00";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenDeliveryWindowHasMinutes() {
        //Invalidate delivery window by adding an offset from the hour.
        this.deliveryWindowStart = "20:01";

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "delivery window" + ": " + deliveryWindowStart + ". " +
                CustomerDetails.DEL_WIN_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }

    @Test
    public void shouldThrowException_whenCustomMessageTooLong() {
        //Invalidate custom message by making it too long.
        this.customMessage += "a".repeat(CustomerDetails.MAX_CUSTOM_MSG_LEN - customMessage.length() + 1);

        @SuppressWarnings("unused")
        InvalidCustomerDetailsException ex = assertThrows(InvalidCustomerDetailsException.class, () ->
            new CustomerDetails(name, email, phone, address, postcode,
                                deliveryDate, deliveryWindowStart, customMessage));

        String expected = "Invalid " + "custom message" + ": " + CustomerDetails.trimLongString(customMessage) +
                            ". " + CustomerDetails.CUST_MSG_HELP_MSG;
        assertEquals(ex.getMessage(), expected);
    }
}
