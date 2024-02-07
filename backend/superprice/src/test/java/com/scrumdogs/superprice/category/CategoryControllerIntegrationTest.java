package com.scrumdogs.superprice.category;

import com.scrumdogs.superprice.SuperpriceApplication;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SuperpriceApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

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
    public void all_categories() throws Exception {
        mvc.perform(get("/v1/categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Fruit & Vegetables")))
                .andExpect(jsonPath("$[0].children[1].name", is("Vegetables")))
                .andExpect(jsonPath("$[1].name", is("Poultry, Meat & Seafood")))
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    public void get_categories_from_child() throws Exception {
        mvc.perform(get("/v1/categories?root=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Dairy, Egg & Fridge")))
                .andExpect(jsonPath("$[0].children[0].name", is("Dairy")))
                .andExpect(jsonPath("$[0].children[1].name", is("Eggs")))
                .andExpect(jsonPath("$[0].children[2].name", is("Fridge")))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].children", hasSize(3)));
    }

    @Test
    public void get_categories_from_leaf() throws Exception {
        mvc.perform(get("/v1/categories?root=48").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Cream")))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].children", hasSize(0)));

    }

    @Test
    void get_category_not_found() throws Exception {
        mvc.perform(get("/v1/categories?root=404").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")));
    }

}
