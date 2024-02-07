package com.scrumdogs.superprice.order;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scrumdogs.superprice.SuperpriceApplication;
import com.scrumdogs.superprice.order.cart.InsufficientStockException;
import com.scrumdogs.superprice.order.cart.InvalidOrderItemException;
import com.scrumdogs.superprice.order.cart.OrderItem;
import com.scrumdogs.superprice.order.controllers.OrderNotCreatedException;
import com.scrumdogs.superprice.order.customer.CustomerDetails;
import java.util.ArrayList;
import java.util.List;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = SuperpriceApplication.class
)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

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

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ObjectNode root = objectMapper.createObjectNode();
  private final ArrayNode itemsNode = objectMapper.createArrayNode();

  @Autowired
  MockMvc mvc;

  @Autowired
  Flyway flyway;

  @BeforeEach
  void setup() {
    //These are all valid details, the tests below will selectively invalidate one of them to run the test
    this.name = "Scrumdad Millionarius";
    this.email = "big.papi.scrum03@gmail.com";
    this.phone = "61372786323";
    this.address = "3/30 Scrumdog Lane";
    this.postcode = "3003";
    this.deliveryDate = "3023-12-31";
    this.deliveryWindowStart = "16:00";
    this.customMessage =
      "Just yeet it in the general vicinity of the property - aka Amazon S.O.P.";

    //Create a list of valid order items as a base case.
    this.items.add(new OrderItem(1, 13));
    this.items.add(new OrderItem(33, 1));
    this.items.add(new OrderItem(18, 19));
    this.items.add(new OrderItem(56, 3));
    this.items.add(new OrderItem(6, 9));
    this.expectedTotalInCents = 40740;

    flyway.migrate();
  }

  @AfterEach
  public void tearDown() {
    flyway.clean();
  }

  @Test
  void shouldReturnOrder_whenValidInformationGiven() throws Exception {
    buildRequestBody(this.root);

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
      )
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.details.name", is(this.name)))
      .andExpect(jsonPath("$.details.email", is(this.email)))
      .andExpect(jsonPath("$.details.phone", is(this.phone)))
      .andExpect(jsonPath("$.details.address", is(this.address)))
      .andExpect(jsonPath("$.details.postcode", is(this.postcode)))
      .andExpect(jsonPath("$.details.deliveryDate", is(this.deliveryDate)))
      .andExpect(
        jsonPath("$.details.deliveryWindowStart", is(this.deliveryWindowStart))
      )
      .andExpect(jsonPath("$.details.customMessage", is(this.customMessage)))
      .andExpect(jsonPath("$.cart.totalInCents", is(this.expectedTotalInCents)))
      .andExpect(jsonPath("$.status", is(OrderStatus.EMAILED.name())));
  }

  @Test
  public void shouldCreateApprovedOrder_withDefaultCustomMessage_whenCustomMessageBlank()
    throws Exception {
    //Make the custom message blank.
    this.customMessage = "";

    buildRequestBody(this.root);

    System.out.println(root);

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
      )
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.details.name", is(this.name)))
      .andExpect(jsonPath("$.details.email", is(this.email)))
      .andExpect(jsonPath("$.details.phone", is(this.phone)))
      .andExpect(jsonPath("$.details.address", is(this.address)))
      .andExpect(jsonPath("$.details.postcode", is(this.postcode)))
      .andExpect(jsonPath("$.details.deliveryDate", is(this.deliveryDate)))
      .andExpect(
        jsonPath("$.details.deliveryWindowStart", is(this.deliveryWindowStart))
      )
      .andExpect(
        jsonPath(
          "$.details.customMessage",
          is(CustomerDetails.DEFAULT_CUSTOM_MSG)
        )
      )
      .andExpect(jsonPath("$.cart.totalInCents", is(this.expectedTotalInCents)))
      .andExpect(jsonPath("$.status", is(OrderStatus.EMAILED.name())));
  }

  @Test
  void shouldThrowException_whenNameIsEmpty() throws Exception {
    //Invalidate name by making it empty.
    this.name = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenNameHasIllegalCharacter()
    throws Exception {
    //Invalidate name by adding an illegal character.
    this.name += "!";
    buildRequestBody(this.root);

    String expected =
      "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenNameTooLong() throws Exception {
    //Invalidate name by making it too long.
    this.name += "a".repeat(CustomerDetails.MAX_NAME_LEN - name.length() + 1);
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "name" +
      ": " +
      CustomerDetails.trimLongString(name) +
      ". " +
      CustomerDetails.NAME_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenOnlyOneNameProvided() throws Exception {
    //Invalidate name by only giving first name.
    this.name = "Scrumdad";
    buildRequestBody(this.root);

    String expected =
      "Invalid " + "name" + ": " + name + ". " + CustomerDetails.NAME_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmptyEmail() throws Exception {
    //Invalidate email by making it empty.
    this.email = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "email" +
      ": " +
      email +
      ". " +
      CustomerDetails.EMAIL_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenInvalidEmail() throws Exception {
    //Invalidate email by adding illegal character.
    this.email = "#" + email;
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "email" +
      ": " +
      email +
      ". " +
      CustomerDetails.EMAIL_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmailTooLong() throws Exception {
    //Invalidate email by making it too long.
    this.email +=
      "a".repeat(CustomerDetails.MAX_EMAIL_LEN - email.length() + 1);
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "email" +
      ": " +
      CustomerDetails.trimLongString(email) +
      ". " +
      CustomerDetails.EMAIL_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmptyPhone() throws Exception {
    //Invalidate phone by making it empty.
    this.phone = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "phone" +
      ": " +
      phone +
      ". " +
      CustomerDetails.PHONE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenInvalidPhoneCharacters()
    throws Exception {
    //Invalidate phone by adding illegal characters.
    this.phone = "ph" + phone;
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "phone" +
      ": " +
      phone +
      ". " +
      CustomerDetails.PHONE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenPhoneTooLong() throws Exception {
    //Invalidate phone by exceeding the maximum length.
    this.phone +=
      "0".repeat(CustomerDetails.MAX_PHONE_LEN - this.phone.length() + 1);

    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "phone" +
      ": " +
      CustomerDetails.trimLongString(phone) +
      ". " +
      CustomerDetails.PHONE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmptyAddress() throws Exception {
    //Invalidate address by making it empty.
    this.address = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "address" +
      ": " +
      address +
      ". " +
      CustomerDetails.ADDRESS_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenInvalidAddress() throws Exception {
    //Invalidate address by formatting it badly.
    this.address = "Unit 1 123 Main Street";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "address" +
      ": " +
      address +
      ". " +
      CustomerDetails.ADDRESS_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenAddressHasInvalidCharacter()
    throws Exception {
    //Invalidate address by adding an invalid character.
    this.address = "1@123 Main Street";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "address" +
      ": " +
      address +
      ". " +
      CustomerDetails.ADDRESS_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenAddressTooLong() throws Exception {
    //Invalidate address by exceeding the maximum length.
    this.address +=
      "a".repeat(CustomerDetails.MAX_ADDR_LEN - this.address.length() + 1);
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "address" +
      ": " +
      CustomerDetails.trimLongString(address) +
      ". " +
      CustomerDetails.ADDRESS_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmptyPostcode() throws Exception {
    //Invalidate postcode by making it empty.
    this.postcode = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "postcode" +
      ": " +
      postcode +
      ". " +
      CustomerDetails.POSTCODE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenInvalidPostcode() throws Exception {
    //Invalidate postcode by adding illegal character.
    this.postcode = this.postcode.replace("0", "O");
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "postcode" +
      ": " +
      postcode +
      ". " +
      CustomerDetails.POSTCODE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenPostcodeOutOfRange_tooLow()
    throws Exception {
    //Invalidate postcode by putting it outside range.
    this.postcode = String.valueOf(CustomerDetails.POSTCODE_MIN - 1);
    System.out.println(this.postcode);

    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "postcode" +
      ": " +
      postcode +
      ". " +
      CustomerDetails.POSTCODE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenPostcodeOutOfRange_tooHigh()
    throws Exception {
    //Invalidate postcode by putting it outside range.
    this.postcode = String.valueOf(CustomerDetails.POSTCODE_MAX + 1);
    System.out.println(this.postcode);

    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "postcode" +
      ": " +
      postcode +
      ". " +
      CustomerDetails.POSTCODE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenPostcodeTooLong() throws Exception {
    //Invalidate postcode by exceeding the maximum length.
    this.postcode +=
      "0".repeat(CustomerDetails.REQ_POSTCODE_LEN - this.postcode.length() + 1);

    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "postcode" +
      ": " +
      CustomerDetails.trimLongString(postcode) +
      ". " +
      CustomerDetails.POSTCODE_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmptyDeliveryDate() throws Exception {
    //Invalidate delivery date by making it empty.
    this.deliveryDate = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery date" +
      ": " +
      deliveryDate +
      ". " +
      CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryDateHasIllegalCharacter()
    throws Exception {
    //Invalidate delivery date by adding illegal character.
    this.deliveryDate = this.deliveryDate.replace("0", "O");
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery date" +
      ": " +
      deliveryDate +
      ". " +
      CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryDateIsInThePast()
    throws Exception {
    //Invalidate delivery date by making it in the past.
    this.deliveryDate = this.deliveryDate.replace("3023", "1985");
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery date" +
      ": " +
      deliveryDate +
      ". " +
      CustomerDetails.DEL_DATE_IN_PAST_HELP_MSG +
      CustomerDetails.DEL_DATE_STANDARD_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenEmptyDeliveryWindow() throws Exception {
    //Invalidate delivery window by making it empty.
    this.deliveryWindowStart = "";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery window" +
      ": " +
      deliveryWindowStart +
      ". " +
      CustomerDetails.DEL_WIN_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryWindowHasIllegalCharacter()
    throws Exception {
    //Invalidate delivery window by adding illegal character.
    this.deliveryWindowStart = this.deliveryWindowStart.replace("0", "O");
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery window" +
      ": " +
      deliveryWindowStart +
      ". " +
      CustomerDetails.DEL_WIN_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryWindowPoorlyFormatted()
    throws Exception {
    //Invalidate delivery window by removing HH:MM separator.
    this.deliveryWindowStart = this.deliveryWindowStart.replace(":", "");
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery window" +
      ": " +
      deliveryWindowStart +
      ". " +
      CustomerDetails.DEL_WIN_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryWindowOutOfRange()
    throws Exception {
    //Invalidate delivery window by putting it outside range.
    this.deliveryWindowStart = this.deliveryWindowStart.replace("6", "9");
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery window" +
      ": " +
      deliveryWindowStart +
      ". " +
      CustomerDetails.DEL_WIN_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryWindowOutside24H_format()
    throws Exception {
    //Invalidate delivery window by putting it outside 24H format.
    this.deliveryWindowStart = "25:00";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery window" +
      ": " +
      deliveryWindowStart +
      ". " +
      CustomerDetails.DEL_WIN_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenDeliveryWindowHasMinutes()
    throws Exception {
    //Invalidate delivery window by adding an offset from the hour.
    this.deliveryWindowStart = "20:01";
    buildRequestBody(this.root);

    String expected =
      "Invalid " +
      "delivery window" +
      ": " +
      deliveryWindowStart +
      ". " +
      CustomerDetails.DEL_WIN_HELP_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenItemHasZeroInventoryID()
    throws Exception {
    //Invalidate the cart by adding an order item with an inventory ID of 0.
    this.items.add(new OrderItem(0, 3));
    buildRequestBody(this.root);

    String expected =
      InvalidOrderItemException.BASE_ERR_MSG +
      this.items.get(this.items.size() - 1).getInventoryID() +
      InvalidOrderItemException.INV_ID_ERR_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenItemHasNegativeInventoryID()
    throws Exception {
    //Invalidate the cart by adding an order item with a negative inventory ID.
    this.items.add(new OrderItem(-1, 3));
    buildRequestBody(this.root);

    String expected =
      InvalidOrderItemException.BASE_ERR_MSG +
      this.items.get(this.items.size() - 1).getInventoryID() +
      InvalidOrderItemException.INV_ID_ERR_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenItemDoesNotExist() throws Exception {
    //Invalidate the cart by adding an order item which does not exist.
    this.items.add(new OrderItem(Integer.MAX_VALUE, 1));
    buildRequestBody(this.root);

    String expectedPrefix =
      OrderNotCreatedException.ERR_MSG +
      OrderRepositoryImpl.UNKNOWN_CALC_TOTAL_ERROR_MSG;

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", startsWith(expectedPrefix)));
  }

  @Test
  public void shouldThrowException_whenItemHasZeroQuantity() throws Exception {
    //Invalidate the cart by adding an order item with a quantity of 0.
    this.items.add(new OrderItem(1, 0));
    buildRequestBody(this.root);

    String expected =
      InvalidOrderItemException.BASE_ERR_MSG +
      this.items.get(this.items.size() - 1).getInventoryID() +
      InvalidOrderItemException.QTY_ERR_MSG +
      this.items.get(this.items.size() - 1).getQuantity();

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenItemHasNegativeQuantity()
    throws Exception {
    //Invalidate the cart by adding an order item with a negative quantity.
    this.items.add(new OrderItem(1, -3));

    buildRequestBody(this.root);

    String expected =
      InvalidOrderItemException.BASE_ERR_MSG +
      this.items.get(this.items.size() - 1).getInventoryID() +
      InvalidOrderItemException.QTY_ERR_MSG +
      this.items.get(this.items.size() - 1).getQuantity();

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", is(expected)));
  }

  @Test
  public void shouldThrowException_whenInsufficientStock() throws Exception {
    //Invalidate the cart by adding an order item in a ridiculous quantity.
    OrderItem ridic = new OrderItem(1, Integer.MAX_VALUE);
    this.items.add(ridic);

    buildRequestBody(this.root);

    String expectedPrefix =
      InsufficientStockException.ERR_MSG_START + ridic.getInventoryID();

    mvc
      .perform(
        post("/v1/cart/checkout")
          .content(asJsonString(root))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
      )
      .andExpect(jsonPath("$.detail", startsWith(expectedPrefix)));
  }

  public static String asJsonString(final Object obj) {
    //Re-used from lectorial code and group assignment
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void buildRequestBody(ObjectNode root) {
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
  }
}
