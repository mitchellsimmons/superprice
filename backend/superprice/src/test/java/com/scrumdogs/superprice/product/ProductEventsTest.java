package com.scrumdogs.superprice.product;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductEventsTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    Flyway flyway;


    @BeforeEach
    public void setUp() {
        flyway.migrate();
    }

    @AfterEach
    public void tearDown() {
        flyway.clean();
    }
    @Test
    void sse_notifications_changes(@Autowired TestRestTemplate restTemplate) {
        String body = restTemplate.getForObject("/v1/products/notifications?timeout=40000&time=22:36:30", String.class);
        String expected = "[{\"inventoryId\":8214,\"productId\":16,\"storeId\":244,\"categoryId\":29,\"name\":\"Lamb Leg Per Kg\",\"description\":\"Delicious lamb leg cuts\",\"time\":\"22:36:01\",\"priceInCents\":1210,\"stockLevel\":47,\"storeName\":\"FreshGrocer\",\"postcode\":3048},{\"inventoryId\":14594,\"productId\":29,\"storeId\":189,\"categoryId\":46,\"name\":\"2L Full Cream Milk\",\"description\":\"2L full cream milk\",\"time\":\"22:36:03\",\"priceInCents\":325,\"stockLevel\":21,\"storeName\":\"FreshGrocer\",\"postcode\":3037},{\"inventoryId\":26644,\"productId\":53,\"storeId\":359,\"categoryId\":79,\"name\":\"Frozen Meat Pies 4 Pack\",\"description\":\"4-pack of frozen meat pies\",\"time\":\"22:36:04\",\"priceInCents\":550,\"stockLevel\":6,\"storeName\":\"FreshGrocer\",\"postcode\":3071},{\"inventoryId\":8918,\"productId\":17,\"storeId\":453,\"categoryId\":30,\"name\":\"Pork Loin Steaks 500g\",\"description\":\"Tasty pork loin steaks\",\"time\":\"22:36:06\",\"priceInCents\":900,\"stockLevel\":152,\"storeName\":\"MegaMart\",\"postcode\":3090},{\"inventoryId\":9164,\"productId\":18,\"storeId\":204,\"categoryId\":31,\"name\":\"Tasmanian Salmon Per Kg\",\"description\":\"Premium Tasmanian Atlantic salmon\",\"time\":\"22:36:06\",\"priceInCents\":4500,\"stockLevel\":57,\"storeName\":\"FreshGrocer\",\"postcode\":3040},{\"inventoryId\":26160,\"productId\":52,\"storeId\":370,\"categoryId\":78,\"name\":\"Pork Dumplings 1 Kg\",\"description\":\"1 Kg of tasty pork dumplings\",\"time\":\"22:36:08\",\"priceInCents\":1170,\"stockLevel\":52,\"storeName\":\"QuickGrocery\",\"postcode\":3073},{\"inventoryId\":8961,\"productId\":17,\"storeId\":496,\"categoryId\":30,\"name\":\"Pork Loin Steaks 500g\",\"description\":\"Tasty pork loin steaks\",\"time\":\"22:36:09\",\"priceInCents\":860,\"stockLevel\":200,\"storeName\":\"Supermart\",\"postcode\":3099},{\"inventoryId\":9529,\"productId\":19,\"storeId\":74,\"categoryId\":32,\"name\":\"Small Rock Lobster\",\"description\":\"Cooked and thawed small Rock lobster\",\"time\":\"22:36:15\",\"priceInCents\":3050,\"stockLevel\":26,\"storeName\":\"FreshGrocer\",\"postcode\":3014},{\"inventoryId\":21112,\"productId\":42,\"storeId\":272,\"categoryId\":62,\"name\":\"Pearl Barley 500g\",\"description\":\"500g of pearl barley\",\"time\":\"22:36:20\",\"priceInCents\":175,\"stockLevel\":94,\"storeName\":\"Grocery Haven\",\"postcode\":3054},{\"inventoryId\":7251,\"productId\":14,\"storeId\":271,\"categoryId\":27,\"name\":\"Turkey Breast Per Kg\",\"description\":\"Lean and flavorful turkey breast\",\"time\":\"22:36:21\",\"priceInCents\":2005,\"stockLevel\":158,\"storeName\":\"Supermart\",\"postcode\":3054},{\"inventoryId\":20191,\"productId\":40,\"storeId\":341,\"categoryId\":60,\"name\":\"Penne Pasta 500g\",\"description\":\"500g of penne pasta\",\"time\":\"22:36:23\",\"priceInCents\":235,\"stockLevel\":59,\"storeName\":\"Supermart\",\"postcode\":3068},{\"inventoryId\":3120,\"productId\":6,\"storeId\":100,\"categoryId\":16,\"name\":\"Strawberries Punnet 300g\",\"description\":\"Plump and juicy strawberries\",\"time\":\"22:36:27\",\"priceInCents\":240,\"stockLevel\":98,\"storeName\":\"QuickGrocery\",\"postcode\":3019},{\"inventoryId\":3611,\"productId\":7,\"storeId\":96,\"categoryId\":17,\"name\":\"White Potatoes Per Kg\",\"description\":\"White washed potatoes\",\"time\":\"22:36:28\",\"priceInCents\":345,\"stockLevel\":196,\"storeName\":\"Supermart\",\"postcode\":3019}]";
        assertThat(body.substring(32).trim()).isEqualTo(expected);
    }
}
