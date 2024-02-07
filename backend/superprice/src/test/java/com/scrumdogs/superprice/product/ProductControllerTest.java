package com.scrumdogs.superprice.product;

import com.scrumdogs.superprice.product.controllers.ProductController;
import com.scrumdogs.superprice.product.controllers.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductControllerTest {

    ProductController controller;
    ProductService service;

    @BeforeEach
    void setup() {
        this.service = mock(ProductService.class);
        this.controller = new ProductController(this.service);
    }

    /**
     * All Products
     */
    @Test
    void should_returnEmpty_When_noRecords() {
        when(this.service.getAllProducts()).thenReturn(new ArrayList<>());
        assertEquals(0, this.controller.all().size());
    }

    @Test
    void should_returnAllProducts_When_availableInService() {
        when(this.service.getAllProducts())
                .thenReturn(List.of(
                        new Product(1L, "Product 1", 123L, "Product description"),
                        new Product(2L, "Product 2", 124L, "Product description 2")
                ));
        assertEquals(2, this.controller.all().size());
    }

    /**
     * Products by ID
     */
    @Test
    void should_returnProductWithMatchingId_When_getProductById() {
        Product product = new Product(1L, "Product 1", 123L, "Product description");
        when(this.service.getProductById(1L)).thenReturn(Optional.of(product));
        assertEquals(product.id(), controller.id(1L).id());
    }

    /**
     * Product Inventory By ID
     */
    @Test
    void should_returnProductInventory_When_availableInServiceById() {
        List<ProductInventory> expected = Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3050L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3100L)
        );
        when(this.service.getProductInventoryById(2L)).thenReturn(Optional.of(expected));
        assertEquals(3, this.controller.idInventory(2L, null).size());
    }
    @Test
    void should_throwProductNotFoundException_whenNotAvailableById() {
        when(this.service.getProductInventoryById(33L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.idInventory(33L, null);});
    }

    /**
     * Product Inventory By ID and Postcode
     */
    @Test
    void should_returnProductInventory_When_availableInServiceByIdAndPostcode() {
        List<ProductInventory> expected = Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3050L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3100L)
        );
        when(this.service.getProductInventoryByIdAndPostcode(2L, 3000L)).thenReturn(Optional.of(expected));
        assertEquals(3, this.controller.idInventory(2L, 3000L).size());
    }
    @Test
    void should_throwProductNotFoundException_whenNotAvailableByIdAndPostcode() {
        when(this.service.getProductInventoryByIdAndPostcode(33L, 300011L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.idInventory(33L, 300011L);});
    }
//    Fixme: Not sure if this test is needed
//    @Test
//    void should_throwProductNotFoundException_whenNotAvailableByPostcode() {
//        when(this.service.getProductInventoryByIdAndPostcode(1L, 31L)).thenReturn(Optional.empty());
//        assertThrows(ProductNotFoundException.class, () -> {this.controller.idInventory(1L, 31L);});
//    }

    /**
     * Product Inventory By Name
     */
    @Test
    void should_returnProductInventory_When_availableInServiceByName() {
        List<ProductInventory> expected = Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3050L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3100L)
        );
        when(this.service.getProductInventoryByName("Apple")).thenReturn(Optional.of(expected));
        assertEquals(3, this.controller.nameInventory("Apple", null).size());
    }
    @Test
    void should_throwProductNotFoundException_whenNotAvailableByName() {
        when(this.service.getProductInventoryByName("Grape")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.nameInventory("Grape", null);});
    }

    /**
     * Product Inventory By Name and Postcode
     */
    @Test
    void should_returnProductInventory_When_availableInServiceByNameAndPostcode() {
        List<ProductInventory> expected = Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3050L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3100L)
        );
        when(this.service.getProductInventoryByNameAndPostcode("Apple", 3000L)).thenReturn(Optional.of(expected));
        assertEquals(3, this.controller.nameInventory("Apple", 3000L).size());
    }
    @Test
    void should_throwProductNotFoundException_whenNotAvailableByNameAndPostcode() {
        when(this.service.getProductInventoryByNameAndPostcode("salt", 300011L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.nameInventory("salt", 300011L);});
    }

//    Fixme: Not sure if this test is needed
//    @Test
//    void should_throwProductNotFoundException_whenNotAvailableByPostcode() {
//        when(this.service.getProductInventoryByIdAndPostcode(1L, 31L)).thenReturn(Optional.empty());
//        assertThrows(ProductNotFoundException.class, () -> {this.controller.idInventory(1L, 31L);});
//    }

    /**
     * Product by Category
     */
    @Test
    void should_returnProducts_When_availableInServiceByCategory() {
        List<Product> expected = (List.of(new Product(1L, "Product 1", 1L, "Product description")));
        when(this.service.getProductsByCategory(1L))
                .thenReturn(Optional.of(expected));
        assertEquals(1, this.controller.category(1L).size());
    }

    @Test
    void should_throwProductNotFoundException_When_emptyCategoryIdGiven() {
        when(this.service.getProductsByCategory(8L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.category(8L);});
    }

    /**
     * Product By Name
     */
    @Test
    void should_returnProducts_When_availableInServiceByName() {
        List<Product> expected = (List.of(new Product(1L, "Product 1", 1L, "Product description")));
        when(this.service.getProductsByName("Product 1"))
                .thenReturn(Optional.of(expected));
        assertEquals(1, this.controller.name("Product 1").size());
    }

    /**
     * Popular products
     */
    @Test
    void whenFindPopular_shouldReturn() {
        // expected
        List<ProductInventory> expected = Arrays.asList(
                new ProductInventory(136L, 28L, 1L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("20:18:52"), 135L, 178L, "Supermart", 3000L),
                new ProductInventory(208L, 42L, 3L, 62L, "Pearl Barley 500g", "500g of pearl barley", LocalTime.parse("19:10:02"), 155L, 188L, "MegaMart", 3000L),
                new ProductInventory(470L, 39L, 5L, 59L, "Caster Sugar 1 Kg", "1 Kg of high-quality caster sugar", LocalTime.parse("11:29:59"), 170L, 35L, "QuickGrocery", 3000L)
        );

        // mock
        when(this.service.getPopularProductsInventory(null)).thenReturn(Optional.of(expected));

        // test
        assertEquals(expected, this.controller.popular(null));
    }

    @Test
    void whenFindPopularByPostcode_shouldReturn() {
        // expected
        List<ProductInventory> expected = Arrays.asList(
                new ProductInventory(136L, 28L, 1L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("20:18:52"), 135L, 178L, "Supermart", 3000L),
                new ProductInventory(208L, 42L, 3L, 62L, "Pearl Barley 500g", "500g of pearl barley", LocalTime.parse("19:10:02"), 155L, 188L, "MegaMart", 3000L),
                new ProductInventory(470L, 39L, 5L, 59L, "Caster Sugar 1 Kg", "1 Kg of high-quality caster sugar", LocalTime.parse("11:29:59"), 170L, 35L, "QuickGrocery", 3000L)
        );

        // mock
        when(this.service.getPopularProductsInventory(3000L)).thenReturn(Optional.of(expected));

        // test
        assertEquals(expected, this.controller.popular(3000L));
    }

    @Test
    void should_throwProductNotFoundException_whenNotAvailableByPopularProducts() {
        when(this.service.getPopularProductsInventory(9000L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.popular(9000L);});
    }

    @Test
    void should_throwProductNotFoundException_When_emptyNamedGiven() {
        when(this.service.getProductsByName("Name")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> {this.controller.name("Name");});
    }


//    just testing that an emitter is returned
//    unsure how to test that data is sent here
//    dale has agreed this is okay
    @Test
    void should_sendEvent_When_times() {
        List<ProductInventory> expected = List.of(
                new ProductInventory(213L, 43L, 3L, 66L, "Chocolate Bar 100g",	"Indulgent 100g chocolate bar", LocalTime.parse("00:00:00"),	245L,	103L,	"MegaMart",	3000L));
        when(this.service.getPriceChanges(LocalTime.NOON.minusSeconds(30), LocalTime.NOON)).thenReturn(expected);
        SseEmitter emitter = new SseEmitter(-1L);
        assertEquals(emitter.getClass(), controller.streamNotifications(null, null).getClass());

    }


}

