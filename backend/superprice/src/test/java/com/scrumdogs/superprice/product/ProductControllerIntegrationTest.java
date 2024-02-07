package com.scrumdogs.superprice.product;

import com.scrumdogs.superprice.SuperpriceApplication;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import static org.hamcrest.Matchers.lessThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SuperpriceApplication.class)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

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

    /**
     * All Products
     */
    @Test
    void all_products() throws Exception {
        mvc.perform(get("/v1/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jazz Apples Per Kg")))
                .andExpect(jsonPath("$[1].categoryId", is(12)))
                .andExpect(jsonPath("$[1].description", is("Crisp and sweet red apples")));

    }

    /**
     * Product by ID
     */
    @Test
    void get_product_id_2() throws Exception{
        mvc.perform(get("/v1/products?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Jazz Apples Per Kg")))
                .andExpect(jsonPath("$.categoryId", is(12)))
                .andExpect(jsonPath("$.description", is("Crisp and sweet red apples")));
    }

    @Test
    void get_product_not_found() throws Exception {
        mvc.perform(get("/v1/products?id=404").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    public Boolean isBefore(String time) {
        return LocalTime.now().isBefore(LocalTime.parse(time));
    }


    /**
     * Product Inventory by ID
     */
    @Test
    void get_productInventory_id_1() throws Exception {
//        if testing whether different product inventories are returned at different times
        if (LocalTime.now().isBefore(LocalTime.parse("00:02:55"))) {
            //            expect a 404 as before this time no products are available
            mvc.perform(get("/v1/products/inventory?id=1").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title", is("Not Found")));
        }
        else if (LocalTime.now().isBefore(LocalTime.parse("05:12:29"))) {
//            expect a 404 as before this time no products are available
            mvc.perform(get("/v1/products/inventory?id=1").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(lessThan(97))));
        } else if (LocalTime.now().isBefore(LocalTime.parse("05:56:31"))) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(279)));
        } else if (isBefore("08:38:41")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(4)));
        } else if (isBefore("08:56:42")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(3)));
        } else if (isBefore("12:25:47")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:16:51")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:49:33")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("21:47:23")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("23:06:30")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        } else if (isBefore("23:40:14")) {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(1)));
        } else {
            mvc.perform(get("/v1/products/inventory?id=1"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(276)));
        }
    }

    @Test
    void get_productInventoryById_not_found() throws Exception {
        mvc.perform(get("/v1/products/inventory?id=404").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    /**
     * Product Inventory By ID and Postcode
     */
    @Test
    void get_productInventory_id_1_postcode_3000() throws Exception {
//        if testing whether different product inventories are returned at different times
        if (LocalTime.now().isBefore(LocalTime.parse("05:12:29"))) {
//            expect a 404 as before this time no products are available
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title", is("Not Found")));
        } else if (LocalTime.now().isBefore(LocalTime.parse("05:56:31"))) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(279)));
        } else if (isBefore("08:38:41")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(4)));
        } else if (isBefore("08:56:42")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(3)));
        } else if (isBefore("12:25:47")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:16:51")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:49:33")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("21:47:23")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("23:06:30")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        } else if (isBefore("23:40:14")) {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(1)));
        } else {
            mvc.perform(get("/v1/products/inventory?id=1&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(276)));
        }
    }
    @Test
    void get_productInventoryByIdAndPostcode_not_found() throws Exception {
        mvc.perform(get("/v1/products/inventory?id=404&loc=404").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    /**
     * Product Inventory by Name
     */
    @Test
    void get_productInventory_name_banana() throws Exception {
//        testing whether different product inventories are returned at different times
        if (LocalTime.now().isBefore(LocalTime.parse("00:02:55"))) {
            //            expect a 404 as before this time no products are available
            mvc.perform(get("/v1/products/inventory?name=banana").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title", is("Not Found")));
        }
        else if (LocalTime.now().isBefore(LocalTime.parse("05:12:29"))) {
//            expect a 404 as before this time no products are available
            mvc.perform(get("/v1/products/inventory?name=banana").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(lessThan(97))));
        } else if (LocalTime.now().isBefore(LocalTime.parse("05:56:31"))) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(279)));
        } else if (isBefore("08:38:41")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(4)));
        } else if (isBefore("08:56:42")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(3)));
        } else if (isBefore("12:25:47")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:16:51")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:49:33")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("21:47:23")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("23:06:30")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        } else if (isBefore("23:40:14")) {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(1)));
        } else {
            mvc.perform(get("/v1/products/inventory?name=banana"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(276)));
        }
    }

    @Test
    void get_productInventoryByName_not_found() throws Exception {
        mvc.perform(get("/v1/products/inventory?name=salt").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }
    /**
     * Product Inventory by Name and Postcode
     */
    @Test
    void get_productInventory_name_banana_postcode_3000() throws Exception {
//        testing whether different product inventories are returned at different times
        if (LocalTime.now().isBefore(LocalTime.parse("05:12:29"))) {
            //            expect a 404 as before this time no products are available
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title", is("Not Found")));
        } else if (LocalTime.now().isBefore(LocalTime.parse("05:56:31"))) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(279)));
        } else if (isBefore("08:38:41")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(4)));
        } else if (isBefore("08:56:42")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(3)));
        } else if (isBefore("12:25:47")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:16:51")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(277)));
        } else if (isBefore("17:49:33")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("21:47:23")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        }  else if (isBefore("23:06:30")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(2)));
        } else if (isBefore("23:40:14")) {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(1)));
        } else {
            mvc.perform(get("/v1/products/inventory?name=banana&loc=3000"))
                    .andExpect(status().isOk())
                    .andExpect(
                            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].inventoryId", is(276)));
        }
    }

    @Test
    void get_productInventoryByNameAndPostcode_not_found() throws Exception {
        mvc.perform(get("/v1/products/inventory?name=salt&loc=404").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    /**
     * Product by Name
     */
    @Test
    void get_product_name() throws Exception {
        mvc.perform(get("/v1/products?name=Apple")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Jazz Apples Per Kg")))
                .andExpect(jsonPath("$[0].categoryId", is(12)))
                .andExpect(jsonPath("$[0].description", is("Crisp and sweet red apples")));
    }

    @Test
    void get_product_name_not_found() throws Exception {
        mvc.perform(get("/v1/products?name=Mark").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    /**
     * Product by category
     */
    @Test
    public void get_product_category() throws Exception {
        mvc.perform(get("/v1/products?category=12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Jazz Apples Per Kg")))
                .andExpect(jsonPath("$[0].categoryId", is(12)))
                .andExpect(jsonPath("$[0].description", is("Crisp and sweet red apples")));
    }

    @Test
    void get_product_category_not_found() throws Exception {
        mvc.perform(get("/v1/products?category=404").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

    /**
     * Popular Items
     */
    @Test
    void get_popular_items() throws Exception {
//        number of items changes as new items are available/ their "time" is not greater than the current time
        int numItems = 10;
        if (LocalTime.now().isBefore(LocalTime.parse("00:03:56"))) {
            numItems = 1;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:04:06"))) {
            numItems = 2;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:04:09"))) {
            numItems = 3;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:04:31"))) {
            numItems = 4;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:08:44"))) {
            numItems = 5;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:10:31"))) {
            numItems = 6;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:20:19"))) {
            numItems = 7;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:22:32"))) {
            numItems = 8;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:23:00")) || LocalTime.now().isBefore(LocalTime.parse("00:38:15"))) {
            numItems = 9;
        }
        mvc.perform(get("/v1/products/popular").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(numItems)));
    }

    @Test
    //  TODO Could extend this test to check the right items are returned, but the effort/reward is v. low.
    void get_popular_items_by_postcode() throws Exception {
//        number of items changes as new items are available/ their "time" is not greater than the current time
        int numItems = 10;
        if (LocalTime.now().isBefore(LocalTime.parse("00:03:56"))) {
            numItems = 1;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:04:06"))) {
            numItems = 2;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:04:09"))) {
            numItems = 3;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:04:31"))) {
            numItems = 4;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:08:44"))) {
            numItems = 5;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:10:31"))) {
            numItems = 6;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:20:19"))) {
            numItems = 7;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:22:32"))) {
            numItems = 8;
        } else if (LocalTime.now().isBefore(LocalTime.parse("00:23:00")) || LocalTime.now().isBefore(LocalTime.parse("00:38:15"))) {
            numItems = 9;
        }
        mvc.perform(get("/v1/products/popular?loc=3000").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(numItems)));
    }

}
