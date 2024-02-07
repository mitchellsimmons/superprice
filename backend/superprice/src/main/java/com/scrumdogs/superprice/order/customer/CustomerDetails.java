package com.scrumdogs.superprice.order.customer;

import lombok.Getter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Conveniently stores the customer details for an Order.
 * Although not mapped directly to a Customer table in the DB, doing this prevents the Order from needing to perform
 * error-checking of customer details at the time of Order creation.
 * This also allows some scope for potential future implementation of login, if desired. */

@Getter
public class CustomerDetails {
    //Constants to govern the maximum length of fields
    public static final int MAX_NAME_LEN = 32;
    public static final int MAX_EMAIL_LEN = 255;
    public static final int MAX_PHONE_LEN = 12;     //Includes the possibility of '+614 or +613'
    public static final int MAX_ADDR_LEN = 255;
    public static final int REQ_POSTCODE_LEN = 4;
    public static final int MAX_CUSTOM_MSG_LEN = 255;

    //Delivery windows must be a 4-hr window within these acceptable limits
    public static final String DEL_WIN_12AM = "00:00";
    public static final String DEL_WIN_8PM = "20:00";

    //Postcodes must be between these numeric limits
    public static final int POSTCODE_MIN = 3000;
    public static final int POSTCODE_MAX = 3100;

    //Provide a default custom message if none given by customer
    public static final String DEFAULT_CUSTOM_MSG = "No custom message provided by customer.";

    public static final String NAME_HELP_MSG =  "Please give first and last names - letters and hyphens only (" +
                                                    MAX_NAME_LEN + " characters max).";

    public static final String EMAIL_HELP_MSG =  "Please give a valid email (" + MAX_EMAIL_LEN + " characters max).";

    public static final String PHONE_HELP_MSG =  "Please give a valid AU mobile or MEL landline phone number (" +
                                                    MAX_PHONE_LEN + " digits max - can start with +61, 61 or 0).";

    public static final String ADDRESS_HELP_MSG = "Please give a valid address - e.g.: 1/123 Main Street (" +
                                                    MAX_ADDR_LEN + " characters max).";

    public static final String POSTCODE_HELP_MSG =  "Please give a valid postcode between " +
                                                        POSTCODE_MIN + " and " + POSTCODE_MAX + " (" +
                                                        REQ_POSTCODE_LEN + " digits max).";

    public static final String DEL_DATE_STANDARD_HELP_MSG = "Please provide a valid delivery date (YYYY-MM-DD). ";

    public static final String DEL_DATE_IN_PAST_HELP_MSG =  "Great Scott, Marty McFly! " +
                                                            "Delivery date cannot be in the past. ";

    public static final String DEL_WIN_HELP_MSG =  "Please give a valid 4-hour delivery window between " +
                                                    DEL_WIN_12AM + " and " +
                                                    DEL_WIN_8PM + " (using 24-hr time format, e.g.: 08:00).";

    public static final String CUST_MSG_HELP_MSG =  "Take it easy, Wordsworth! Let's keep it under " +
                                                    MAX_CUSTOM_MSG_LEN + " characters, please?";

    private String name;
    private String email;
    private String phone;
    private String address;
    private String postcode;
    private String deliveryDate;
    private String deliveryWindowStart;
    private String customMessage;

    public CustomerDetails(String name, String email, String phone, String address, String postcode,
                           String deliveryDate, String deliveryWindowStart, String customMessage){
        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validateAddress(address);
        validatePostcode(postcode);
        validateDeliveryDate(deliveryDate);
        validateDeliveryWindow(deliveryWindowStart);
        validateCustomMessage(customMessage);
    }

    private void validateName(String name) throws InvalidCustomerDetailsException {
        //Matches first and last names, wih hyphens allowed...This one's for you, Geordie Elliot-Kerr! <3
        //https://regex101.com/r/zN5oiH/2
        final String NAME_RGX = "^\\p{L}+-?\\p{L}+ \\p{L}+-?\\p{L}+$";

        Pattern pattern = Pattern.compile(NAME_RGX, Pattern.CASE_INSENSITIVE |
                                                    Pattern.UNICODE_CASE |
                                                    Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(name);

       if(name.isEmpty() || name.length() > MAX_NAME_LEN || !matcher.find()) {
           throw new InvalidCustomerDetailsException("name", trimLongString(name), NAME_HELP_MSG);
       } else {
           this.name = name;
       }
    }

    private void validateEmail(String email) throws InvalidCustomerDetailsException {
        //https://regex101.com/r/A0S8fz/2
        //Matches emails, with dots, hyphens and underscores allowed. Basic domains only (no IP addresses).
        final String EMAIL_RGX = "^([a-z0-9]+[._-]?)+\\w+@(?:[a-z]+[.\\-]?)*(?<=\\.\\w{2,6})$";

        Pattern pattern = Pattern.compile(EMAIL_RGX, Pattern.CASE_INSENSITIVE |
                                                    Pattern.UNICODE_CASE |
                                                    Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(email);

        if(email.isEmpty() || email.length() > MAX_EMAIL_LEN || !matcher.find()) {
            throw new InvalidCustomerDetailsException("email", trimLongString(email), EMAIL_HELP_MSG);
        } else {
            this.email = email;
        }
    }

    private void validatePhone(String phone) throws InvalidCustomerDetailsException {
        //Strip out any whitespace from the phone number.
        phone = phone.replace(" ", "");

        //https://regex101.com/r/UlJ6IK/3
        //Matches basic AUS mobile and MEL landline numbers (+61 optional)
        final String PHONE_RGX = "^(?:\\+?61|0)[34][0-9]{8}$";

        Pattern pattern = Pattern.compile(PHONE_RGX, Pattern.CASE_INSENSITIVE |
                                                    Pattern.UNICODE_CASE |
                                                    Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(phone);

        if(phone.isEmpty() || phone.length() > MAX_PHONE_LEN || !matcher.find()) {
            throw new InvalidCustomerDetailsException("phone", trimLongString(phone), PHONE_HELP_MSG);
        } else {
            this.phone = phone;
        }
    }

    private void validateAddress(String address) throws InvalidCustomerDetailsException {
        //https://regex101.com/r/cS9H8p/1
        //Matches basic street addresses, unit/number format allowed.
        final String ADDRESS_RGX = "^(?:[0-9]{1,4}/)?[0-9]{1,4} (?:[a-z] ?){2,}[a-z]{2,}$";

        Pattern pattern = Pattern.compile(ADDRESS_RGX, Pattern.CASE_INSENSITIVE |
                                                        Pattern.UNICODE_CASE |
                                                        Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(address);

        if(address.isEmpty() || address.length() > MAX_ADDR_LEN || !matcher.find()) {
            throw new InvalidCustomerDetailsException("address", trimLongString(address), ADDRESS_HELP_MSG);
        } else {
            this.address = address;
        }
    }

    private void validatePostcode(String postcode) throws InvalidCustomerDetailsException {
        //https://regex101.com/r/BuSeS0/2
        //Matches 4-digit postcodes between 3000 and 3100
        final String POSTCODE_RGX = "^3[01][0-9]{2}$";

        Pattern pattern = Pattern.compile(POSTCODE_RGX, Pattern.CASE_INSENSITIVE |
                                                        Pattern.UNICODE_CASE |
                                                        Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(postcode);

        final int postcodeNumeric;
        try {
            postcodeNumeric = Integer.parseInt(postcode);
        } catch(NumberFormatException e) {
            throw new InvalidCustomerDetailsException("postcode", trimLongString(postcode), POSTCODE_HELP_MSG);
        }

        if(postcode.length() != REQ_POSTCODE_LEN || postcodeNumeric < POSTCODE_MIN || postcodeNumeric > POSTCODE_MAX ||
                !matcher.find()) {

            throw new InvalidCustomerDetailsException("postcode", trimLongString(postcode), POSTCODE_HELP_MSG);
        } else {
            this.postcode = postcode;
        }
    }

    private void validateDeliveryDate(String deliveryDate) {
        //Leave the delivery date as a String, but use LocalDate to verify its format is acceptable.
        try {
            //Matches dates in ISO_LOCAL_DATE (ISO-8601) format: YYYY-MM-DD
            LocalDate date = LocalDate.parse(deliveryDate);

            if(date.isBefore(LocalDate.now())) {
                throw new DateTimeException(DEL_DATE_IN_PAST_HELP_MSG);
            } else {
                this.deliveryDate = deliveryDate;
            }

        } catch(DateTimeParseException e) {
            throw new InvalidCustomerDetailsException("delivery date", trimLongString(deliveryDate),
                                                        DEL_DATE_STANDARD_HELP_MSG);
        } catch(DateTimeException e) {
            throw new InvalidCustomerDetailsException("delivery date", trimLongString(deliveryDate),
                                                        e.getMessage() + DEL_DATE_STANDARD_HELP_MSG);
        }
    }

    private void validateDeliveryWindow(String deliveryWindowStart) throws InvalidCustomerDetailsException {
        //Matches any of the following accepted times: 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
        final String DELIVERY_RGX = "^(00|04|08|12|16|20):00$";

        Pattern pattern = Pattern.compile(DELIVERY_RGX);
        Matcher matcher = pattern.matcher(deliveryWindowStart);

        //Check that the delivery window falls within accepted 4hr ranges - else throw error.
        if(!matcher.find()) {
            throw new InvalidCustomerDetailsException("delivery window",
                                                        trimLongString(deliveryWindowStart), DEL_WIN_HELP_MSG);
        } else {
            this.deliveryWindowStart = deliveryWindowStart;
        }
    }

    private void validateCustomMessage(String customMessage) {
        if(customMessage.isEmpty()) {
            this.customMessage = DEFAULT_CUSTOM_MSG;
        } else if(customMessage.length() > MAX_CUSTOM_MSG_LEN) {
            throw new InvalidCustomerDetailsException("custom message", trimLongString(customMessage),
                                                        CUST_MSG_HELP_MSG);
        } else {
            this.customMessage = customMessage;
        }
    }

    public static String trimLongString(String value) {
        //Make sure the error message string isn't too long to display neatly...
        if(value.length() > MAX_NAME_LEN) {
            value = value.substring(0, MAX_NAME_LEN - 1) + "..";
        }

        return value;
    }
}
