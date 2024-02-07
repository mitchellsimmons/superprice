package com.scrumdogs.superprice.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductService service;
    private final ProductRepository productRepository = mock(ProductRepository.class);

    @BeforeEach
    void setup() {
        this.service = new ProductServiceImpl(productRepository);
    }

    /**
     * All Products
     */
    @Test
    void should_returnEmpty_When_noRecords() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    void should_returnProducts_When_availableInService() {
        when(productRepository.findAll())
                .thenReturn(Collections.singletonList(new Product(1L, "Product 1", 123L, "Product description")));
        assertEquals(1, service.getAllProducts().size());
    }

    /**
     * Products by Id
     */
    @Test
    void should_returnProduct_When_ProductIsAvailable() {
        Optional<Product> product = Optional.of(new Product(1L, "Product 1", 123L, "Product description"));
        when(productRepository.findById(1L)).thenReturn(product);
        assertEquals(product, service.getProductById(1L));
    }

    @Test
    void should_returnEmptyOptionalProduct_whenNotAvailable() {
        when(productRepository.findById(33L)).thenReturn(Optional.empty());
        assertFalse(this.service.getProductById(33L).isPresent());
    }

    /**
     * Product Inventory by id
     */
    @Test
    void should_returnProductInventory_whenProductIsAvailableById() {
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3050L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3100L)
        ));
        when(productRepository.findInventoryById(2L, LocalTime.now().withNano(0))).thenReturn(expected);
        assertEquals(expected, service.getProductInventoryById(2L));
    }

    @Test
    void should_returnEmptyOptionalInventory_whenNotAvailableById() {
        when(productRepository.findInventoryById(33L, LocalTime.now())).thenReturn(Optional.empty());
        assertFalse(this.service.getProductInventoryById(33L).isPresent());
    }

    /**
     * Product Inventory by id and Postcode
     */
    @Test
    void should_returnProductInventory_whenProductIsAvailableByIdAndPostcode() {
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3000L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3000L)
        ));
        when(productRepository.findInventoryByIdAndPostcode(2L, 3000L,LocalTime.now().withNano(0))).thenReturn(expected);
        assertEquals(expected, service.getProductInventoryByIdAndPostcode(2L, 3000L));
    }
    @Test
    void should_returnEmptyOptionalInventory_whenNotAvailableByIdAndPostcode() {
        when(productRepository.findInventoryByIdAndPostcode(33L, 3190L, LocalTime.now())).thenReturn(Optional.empty());
        assertFalse(this.service.getProductInventoryByIdAndPostcode(33L, 3190L).isPresent());
    }
//    FIXME: is this needed?
//    @Test
//    void should_returnEmptyOptionalInventory_whenNotAvailableByPostcode() {
//        when(productRepository.findInventoryByIdAndPostcode(1L, 3190L, LocalTime.now())).thenReturn(Optional.empty());
//        assertFalse(this.service.getProductInventoryById(33L).isPresent());
//    }

    /**
     * Product Inventory by name
     */
    @Test
    void should_returnProductInventory_whenProductIsAvailableByName() {
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3050L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3100L)
        ));
        when(productRepository.findInventoryByName("Apple", LocalTime.now().withNano(0))).thenReturn(expected);
        assertEquals(expected, service.getProductInventoryByName("Apple"));
    }

    @Test
    void should_returnEmptyOptionalInventory_whenNotAvailableByName() {
        when(productRepository.findInventoryByName("Grape", LocalTime.now())).thenReturn(Optional.empty());
        assertFalse(this.service.getProductInventoryByName("Grape").isPresent());
    }

    /**
     * Product Inventory By Name and Postcode
     */
    @Test
    void should_returnProductInventory_whenProductIsAvailableByNameAndPostcode() {
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
                new ProductInventory(2L,	2L,	1L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("10:30:00"),	200L,	80L,	"Supermart",	3000L),
                new ProductInventory(8L,	2L,	3L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("09:20:00"),	220L,	70L,	"MegaMart",	3000L),
                new ProductInventory(14L,	2L,	5L,	3L,	"Apple",	"Crispy red apples",	LocalTime.parse("13:00:00"),	230L,	95L,	"QuickGrocery",	3000L)
        ));
        when(productRepository.findInventoryByNameAndPostcode("apple", 3000L,LocalTime.now().withNano(0))).thenReturn(expected);
        assertEquals(expected, service.getProductInventoryByNameAndPostcode("apple", 3000L));
    }
    @Test
    void should_returnEmptyOptionalInventory_whenNotAvailableByNameAndPostcode() {
        when(productRepository.findInventoryByNameAndPostcode("salt", 3190L, LocalTime.now())).thenReturn(Optional.empty());
        assertFalse(this.service.getProductInventoryByNameAndPostcode("salt", 3190L).isPresent());
    }

    /**
     * Product by category
     */
    @Test
    void should_returnProducts_When_availableInServiceByCategory() {
        Optional<List<Product>> expected = Optional.of(List.of(new Product(1L, "Product 1", 1L, "Product description")));
        when(productRepository.findByCategory( 1L))
                .thenReturn(expected);
        assertEquals(expected, service.getProductsByCategory(1L));
    }
    @Test
    void should_returnEmptyOptionalProductByCategory_whenNotAvailable() {
        when(productRepository.findByCategory(33L)).thenReturn(Optional.empty());
        assertFalse(this.service.getProductsByCategory(33L).isPresent());
    }

    /**
     * Product by name
     */
    @Test
    void should_returnProducts_When_availableInServiceByName() {
        Optional<List<Product>> expected = Optional.of(List.of(new Product(1L, "Product 1", 1L, "Product description")));
        when(productRepository.findByName("Product 1"))
                .thenReturn(expected);
        assertEquals(expected, service.getProductsByName("Product 1"));
    }
    @Test
    void should_returnEmptyOptionalProductByName_whenNotAvailable() {
        when(productRepository.findByName("Name")).thenReturn(Optional.empty());
        assertFalse(this.service.getProductsByName("Name").isPresent());
    }

    /**
     * Popular Products
     */
    //TODO: Double check these tests
    @Test
    void should_returnPopularProductsInventory_whenAvailable_andMatchingPostcode() {
        // expected
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
            new ProductInventory(283L, 2L, 3L, 12L, "Jazz Apples Per Kg", "Crisp and sweet red apples", LocalTime.parse("19:58:59"), 400L, 72L, "MegaMart", 3000L),
            new ProductInventory(299L, 5L, 4L, 15L, "Watermelon Per Kg", "Sweet and red seedless watermelon", LocalTime.parse("15:29:18"), 460L, 160L, "FreshGrocer", 3000L),
            new ProductInventory(464L, 38L, 4L, 58L, "Plain Flour 1 Kg", "1 Kg of fine plain flour", LocalTime.parse("01:54:31"), 100L, 170L, "FreshGrocer", 3000L)
        ));

        // mock
        when(productRepository.findPopularProductsInventory(3000L, LocalTime.now().withNano(0))).thenReturn(expected);

        // test
        assertEquals(expected, service.getPopularProductsInventory(3000L));
    }

    @Test
    void should_returnEmptyOptionalPopularProductsInventory_whenNotAvailable() {
        // mock
        when(productRepository.findPopularProductsInventory(9000L, LocalTime.now().withNano(0))).thenReturn(Optional.empty());

        // test
        assertFalse(this.service.getPopularProductsInventory(9000L).isPresent());
    }

    @Test
    void should_returnAllPopularProductsInventory_whenNoPostcodeProvided() {
        // expected
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
            new ProductInventory(136L, 28L, 1L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("20:18:52"), 135L, 178L, "Supermart", 3000L),
            new ProductInventory(208L, 42L, 3L, 62L, "Pearl Barley 500g", "500g of pearl barley", LocalTime.parse("19:10:02"), 155L, 188L, "MegaMart", 3000L),
            new ProductInventory(470L, 39L, 5L, 59L, "Caster Sugar 1 Kg", "1 Kg of high-quality caster sugar", LocalTime.parse("11:29:59"), 170L, 35L, "QuickGrocery", 3000L)
        ));

        // mock
        when(productRepository.findPopularProductsInventory(null, LocalTime.now().withNano(0))).thenReturn(expected);

        // test
        Optional<List<ProductInventory>> result = service.getPopularProductsInventory(null);
        assertEquals(expected, result);
    }

    @Test
    void should_return_empty_if_no_changes() {
        when(productRepository.findChange(LocalTime.now().withNano(0).minusSeconds(30), LocalTime.now().withNano(0))).thenReturn(new ArrayList<>());
        assertEquals(0 , service.getPriceChanges(LocalTime.now().withNano(0).minusSeconds(30), LocalTime.now().withNano(0)).size());
    }

    @Test
    void should_return_productsInventory_if_changes() {
        List<ProductInventory> expected = List.of(
                new ProductInventory(213L, 43L, 3L, 66L, "Chocolate Bar 100g",	"Indulgent 100g chocolate bar", LocalTime.parse("00:00:00"),	245L,	103L,	"MegaMart",	3000L));
        when(productRepository.findChange(LocalTime.now().withNano(0).minusSeconds(30), LocalTime.now().withNano(0))).thenReturn(expected);
        assertEquals(1 , service.getPriceChanges(LocalTime.now().withNano(0).minusSeconds(30), LocalTime.now().withNano(0)).size());
    }
}
