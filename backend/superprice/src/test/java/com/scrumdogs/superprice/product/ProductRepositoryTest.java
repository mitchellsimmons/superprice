package com.scrumdogs.superprice.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setup() {
        flyway.migrate();
        repository = new ProductRepositoryImpl(dataSource);

    }

    public Boolean isBefore(String time) {
        return LocalTime.now().isBefore(LocalTime.parse(time));
    }
    public Boolean isAfter(String time) {
        return LocalTime.now().isAfter(LocalTime.parse(time));
    }

    @AfterEach
    public void teardown() {
        flyway.clean();
    }

    /**
     * All products
     */
    @Test
    public void whenFindAll_thenReturnAllProducts() {
        //  expected results
        List<Product> expected = Arrays.asList(
                new Product(1L, "Cavendish Bananas Per Kg", 11L, "Ripe and fresh bananas"),
                new Product(2L, "Jazz Apples Per Kg", 12L, "Crisp and sweet red apples"),
                new Product(3L, "Naval Oranges Per Kg", 13L, "Tangy and juicy oranges"),
                new Product(4L, "Green Grapes Bag", 14L, "Seedless and succulent green grapes"),
                new Product(5L, "Watermelon Per Kg", 15L, "Sweet and red seedless watermelon"),
                new Product(6L, "Strawberries Punnet 300g", 16L, "Plump and juicy strawberries"),
                new Product(7L, "White Potatoes Per Kg", 17L, "White washed potatoes"),
                new Product(8L, "Carrots 1 Kg", 18L, "Fresh and vibrant carrots"),
                new Product(9L, "Cup Mushrooms Per Kg", 19L, "White and tender cup mushrooms"),
                new Product(10L, "Truss Tomatoes Per Kg", 20L, "Ripe and juicy truss tomatoes"),
                new Product(11L, "Basil Bunch", 21L, "Fragrant and fresh basil"),
                new Product(12L, "Mixed Leaf Salad 300g", 22L, "Assorted fresh salad greens")
        );

        // Tests
        List<Product> all = repository.findAll();
        // Test number of products
        assertEquals(55, all.size());
        // Test product data slice
        assertEquals(expected, all.subList(0, 12));
    }

    /**
     * Find by ID
     */
    @Test
    public void whenFindById_thenReturnProduct() {
        Optional<Product> expected = Optional.of(new Product(3L, "Naval Oranges Per Kg", 13L, "Tangy and juicy oranges"));
        assertEquals(expected, repository.findById(3));
    }

    /**
     * Find by Name
     */
    @Test
    public void whenFindByName_thenReturnMatchingProducts() {
        Optional<List<Product>> expected = Optional.of(Arrays.asList(
            new Product(28L, "1L Full Cream Milk", 46L, "1L full cream milk"),
            new Product(29L, "2L Full Cream Milk", 46L, "2L full cream milk")
        ));

        assertEquals(expected, repository.findByName("milk"));
    }

    @Test
    public void whenFindByName_thenReturnMatchingProduct() {
        Optional<List<Product>> expected = Optional.of(List.of(
                new Product(1L, "Cavendish Bananas Per Kg", 11L, "Ripe and fresh bananas")
        ));

        //  this is also testing for case insensitivity + substring
        assertEquals(expected, repository.findByName("BANAN"));
    }

    @Test
    public void whenFindByName_thenReturnNoMatches() {
        Optional<List<Product>> expected = Optional.empty();

        assertEquals(expected, repository.findByName("salt"));
    }

    /**
     * Find by category
     */
    @Test
    public void whenFindByHighLevelCategory_thenReturnAllMatchingProducts() {
        Optional<List<Product>> expected = Optional.of(Arrays.asList(
                new Product(1L, "Cavendish Bananas Per Kg", 11L, "Ripe and fresh bananas"),
                new Product(2L, "Jazz Apples Per Kg", 12L, "Crisp and sweet red apples"),
                new Product(3L, "Naval Oranges Per Kg", 13L, "Tangy and juicy oranges"),
                new Product(4L, "Green Grapes Bag", 14L, "Seedless and succulent green grapes"),
                new Product(5L, "Watermelon Per Kg", 15L, "Sweet and red seedless watermelon"),
                new Product(6L, "Strawberries Punnet 300g", 16L, "Plump and juicy strawberries"),
                new Product(7L, "White Potatoes Per Kg", 17L, "White washed potatoes"),
                new Product(8L, "Carrots 1 Kg", 18L, "Fresh and vibrant carrots"),
                new Product(9L, "Cup Mushrooms Per Kg", 19L, "White and tender cup mushrooms"),
                new Product(10L, "Truss Tomatoes Per Kg", 20L, "Ripe and juicy truss tomatoes"),
                new Product(11L, "Basil Bunch", 21L, "Fragrant and fresh basil"),
                new Product(12L, "Mixed Leaf Salad 300g", 22L, "Assorted fresh salad greens")
        ));

        assertEquals(expected, repository.findByCategory(1L));
    }
    @Test
    public void whenFindByLowLevelCategory_thenReturnAllMatchingProducts() {
        Optional<List<Product>> expected = Optional.of(List.of(
                new Product(1L, "Cavendish Bananas Per Kg", 11L, "Ripe and fresh bananas")
        ));

        assertEquals(expected, repository.findByCategory(11L));
    }

    @Test
    public void whenFindByCategory_andNoMatches_thenReturnNoMatches() {
        assertFalse(repository.findByCategory(6473829L).isPresent());
    }

    /**
     * Find Inventory by ID
     * As the query return will change dependent on time - we set a time to test
     * The same for findByName
     */
    @Test
    public void whenFindById_time9am_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(3L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:38:41"),	400L,	84L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)));
        Optional<List<ProductInventory>> result = repository.findInventoryById(1L, LocalTime.parse("09:00:00"));
        assertEquals(175, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,3)));
    }
    @Test
    public void whenFindById_time2pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryById(1L, LocalTime.parse("14:00:00"));
        assertEquals(286, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,3)));
    }
    @Test
    public void whenFindById_time10pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(2L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("17:16:51"),	490L,	186L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L),
                new ProductInventory(280L,	1L,	5L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("21:47:23"),	500L,	66L,	"QuickGrocery",	3000L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryById(1L, LocalTime.parse("22:00:00"));
        assertEquals(451, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,4)));
    }

    @Test
    public void whenFindByIdNotAvailable_thenReturnEmpty() {
        Optional<List<ProductInventory>> result = repository.findInventoryById(10000000L, LocalTime.now());
        assertFalse(result.isPresent());
    }

    /**
     * Find by Inventory and Postcode
     */
    @Test
    public void whenFindByIdAndPostcode_time9am_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(3L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:38:41"),	400L,	84L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)));
        assertEquals(expected, repository.findInventoryByIdAndPostcode(1L, 3000L, LocalTime.parse("09:00:00")));
    }
    @Test
    public void whenFindByIdAndPostcode_time2pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)
        ));
        assertEquals(expected, repository.findInventoryByIdAndPostcode(1L, 3000L, LocalTime.parse("14:00:00")));
    }
    @Test
    public void whenFindByIdAndPostcode_time10pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(2L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("17:16:51"),	490L,	186L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L),
                new ProductInventory(280L,	1L,	5L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("21:47:23"),	500L,	66L,	"QuickGrocery",	3000L)
        ));
        assertEquals(expected, repository.findInventoryByIdAndPostcode(1L, 3000L, LocalTime.parse("22:00:00")));
    }

    @Test
    public void whenFindByIdAndPostcodeNotAvailable_thenReturnEmpty() {
        Optional<List<ProductInventory>> result = repository.findInventoryByIdAndPostcode(10000000L, 30111L, LocalTime.now());
        assertFalse(result.isPresent());
    }

    @Test
    public void whenFindByIdAndPostcode_PostcodeNotAvailable_thenReturnEmpty() {
        Optional<List<ProductInventory>> result = repository.findInventoryByIdAndPostcode(1L, 30111L, LocalTime.now());
        assertFalse(result.isPresent());
    }

    /**
     * Find Inventory By Name
     */
    @Test
    public void whenFindByName_time9am_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(3L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:38:41"),	400L,	84L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)));
        Optional<List<ProductInventory>> result = repository.findInventoryByName("bana", LocalTime.parse("09:00:00"));
        assertEquals(175, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,3)));
    }
    @Test
    public void whenFindByName_time2pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryByName("bana", LocalTime.parse("14:00:00"));
        assertEquals(286, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,3)));
    }
    @Test
    public void whenFindByName_time10pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(2L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("17:16:51"),	490L,	186L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L),
                new ProductInventory(280L,	1L,	5L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("21:47:23"),	500L,	66L,	"QuickGrocery",	3000L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryByName("bana", LocalTime.parse("22:00:00"));
        assertEquals(451, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,4)));
    }

    /**
     * Test whether the partial match will return multiple products
     */
    @Test
    public void whenFindByName_time9am_thenReturnProductsInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(412L, 28L, 2L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("01:12:47"), 180L, 120L, "Grocery Haven", 3000L),
                new ProductInventory(138L, 28L, 3L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("05:47:40"), 220L, 96L, "MegaMart", 3000L),
                new ProductInventory(414L, 28L, 4L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("01:50:31"), 190L, 30L, "FreshGrocer", 3000L)
        ));
        Optional<List<ProductInventory>> last_3 = Optional.of(List.of(
                new ProductInventory(14892L, 29L, 487L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("00:21:09"), 340L, 193L, "Grocery Haven", 3097L),
                new ProductInventory(14893L, 29L, 488L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("00:56:39"), 360L, 21L, "MegaMart", 3097L),
                new ProductInventory(14903L, 29L, 498L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("04:48:28"), 305L, 139L, "MegaMart", 3099L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryByName("milk", LocalTime.parse("09:00:00"));
        assertEquals(347, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,3)));
        assertEquals(last_3, Optional.of(result.get().subList(344,347)));
    }
    @Test
    public void whenFindByName_time2pm_thenReturnProductsInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(412L, 28L, 2L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("01:12:47"), 180L, 120L, "Grocery Haven", 3000L),
                new ProductInventory(413L, 28L, 3L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("09:49:19"), 155L, 27L, "MegaMart", 3000L),
                new ProductInventory(414L, 28L, 4L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("01:50:31"), 190L, 30L, "FreshGrocer", 3000L)
        ));
        Optional<List<ProductInventory>> last_3 = Optional.of(List.of(
                new ProductInventory(14903L, 29L, 498L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("04:48:28"), 305L, 139L, "MegaMart", 3099L),
                new ProductInventory(14904L, 29L, 499L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("10:22:07"), 300L, 172L, "FreshGrocer", 3099L),
                new ProductInventory(14905L, 29L, 500L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("09:30:06"), 340L, 7L, "QuickGrocery", 3099L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryByName("milk", LocalTime.parse("14:00:00"));
        assertEquals(563, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,3)));
        assertEquals(last_3, Optional.of(result.get().subList(560,563)));
    }
    @Test
    public void whenFindByName_time10pm_thenReturnProductsInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(136L, 28L, 1L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("20:18:52"), 135L, 178L, "Supermart", 3000L),
                new ProductInventory(137L, 28L, 2L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("18:33:01"), 155L, 126L, "Grocery Haven", 3000L),
                new ProductInventory(413L, 28L, 3L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("09:49:19"), 155L, 27L, "MegaMart", 3000L),
                new ProductInventory(139L, 28L, 4L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("20:15:23"), 145L, 168L, "FreshGrocer", 3000L),
                new ProductInventory(140L, 28L, 5L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("19:49:49"), 155L, 115L, "QuickGrocery", 3000L)
        ));
        Optional<List<ProductInventory>> last_5 = Optional.of(List.of(
                new ProductInventory(14901L, 29L, 496L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("14:28:53"), 275L, 84L, "Supermart", 3099L),
                new ProductInventory(14902L, 29L, 497L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("14:58:09"), 295L, 93L, "Grocery Haven", 3099L),
                new ProductInventory(14903L, 29L, 498L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("04:48:28"), 305L, 139L, "MegaMart", 3099L),
                new ProductInventory(14904L, 29L, 499L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("10:22:07"), 300L, 172L, "FreshGrocer", 3099L),
                new ProductInventory(14905L, 29L, 500L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("09:30:06"), 340L, 7L, "QuickGrocery", 3099L)
        ));
        Optional<List<ProductInventory>> result = repository.findInventoryByName("milk", LocalTime.parse("22:00:00"));
        assertEquals(906, result.get().size());
        assertEquals(expected, Optional.of(result.get().subList(0,5)));
        assertEquals(last_5, Optional.of(result.get().subList(901,906)));
    }


    @Test
    public void whenFindByNameNotAvailable_thenReturnEmpty() {
        Optional<List<ProductInventory>> result = repository.findInventoryByName("salt", LocalTime.now());
        assertFalse(result.isPresent());
    }

    /**
     * Find Inventory By Name and Postcode
     */
    @Test
    public void whenFindByNameAndPostcode_time9am_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(3L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:38:41"),	400L,	84L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)));
        assertEquals(expected, repository.findInventoryByNameAndPostcode("bana", 3000L, LocalTime.parse("09:00:00")));
    }
    @Test
    public void whenFindByNameAndPostcode_time2pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(277L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("08:56:42"),	500L,	127L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L)
        ));
        assertEquals(expected, repository.findInventoryByNameAndPostcode("bana", 3000L, LocalTime.parse("14:00:00")));
    }
    @Test
    public void whenFindByNameAndPostcode_time10pm_thenReturnProductInventory() {
        Optional<List<ProductInventory>> expected = Optional.of(List.of(
                new ProductInventory(2L,	1L,	2L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("17:16:51"),	490L,	186L,	"Grocery Haven",	3000L),
                new ProductInventory(278L,	1L,	3L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("12:25:47"),	420L,	101L,	"MegaMart",	3000L),
                new ProductInventory(4L,	1L,	4L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("05:56:31"),	415L,	122L,	"FreshGrocer",	3000L),
                new ProductInventory(280L,	1L,	5L,	11L,	"Cavendish Bananas Per Kg",	"Ripe and fresh bananas",	LocalTime.parse("21:47:23"),	500L,	66L,	"QuickGrocery",	3000L)
        ));
        assertEquals(expected, repository.findInventoryByNameAndPostcode("bana", 3000L, LocalTime.parse("22:00:00")));
    }

    @Test
    public void whenFindByNameAndPostcodeNotAvailable_thenReturnEmpty() {
        Optional<List<ProductInventory>> result = repository.findInventoryByNameAndPostcode("salt", 30111L, LocalTime.now());
        assertFalse(result.isPresent());
    }

    @Test
    public void whenFindByNameAndPostcode_PostcodeNotAvailable_thenReturnEmpty() {
        Optional<List<ProductInventory>> result = repository.findInventoryByNameAndPostcode("apple", 30111L, LocalTime.now());
        assertFalse(result.isPresent());
    }

    /**
     * Popular products
     */

    @Test
    public void whenFindPopularProductsInventory_andNoPostcode_thenReturnAllPopularProductsInventory() {
        // expected
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
                new ProductInventory(1699L, 3L, 164L, 13L, "Naval Oranges Per Kg", "Tangy and juicy oranges", LocalTime.parse("21:26:27"), 300L, 103L, "FreshGrocer", 3032L),
                new ProductInventory(3144L, 6L, 124L, 16L, "Strawberries Punnet 300g", "Plump and juicy strawberries", LocalTime.parse("21:41:07"), 150L, 61L, "FreshGrocer", 3024L),
                new ProductInventory(2124L, 4L, 94L, 14L, "Green Grapes Bag", "Seedless and succulent green grapes", LocalTime.parse("19:42:47"), 1450L, 146L, "FreshGrocer", 3018L),
                new ProductInventory(871L, 1L, 326L, 11L, "Cavendish Bananas Per Kg", "Ripe and fresh bananas", LocalTime.parse("20:23:01"), 400L, 64L, "Supermart", 3065L),
                new ProductInventory(1373L, 2L, 333L, 12L, "Jazz Apples Per Kg", "Crisp and sweet red apples", LocalTime.parse("21:26:36"), 400L, 94L, "MegaMart", 3066L),
                new ProductInventory(2617L, 5L, 92L, 15L, "Watermelon Per Kg", "Sweet and red seedless watermelon", LocalTime.parse("21:21:07"), 450L, 78L, "Grocery Haven", 3018L),
                new ProductInventory(18928L, 38L, 68L, 58L, "Plain Flour 1 Kg", "1 Kg of fine plain flour", LocalTime.parse("20:49:27"), 100L, 42L, "MegaMart", 3013L),
                new ProductInventory(14162L, 28L, 252L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("21:27:15"), 130L, 159L, "Grocery Haven", 3050L),
                new ProductInventory(19402L, 39L, 47L, 59L, "Caster Sugar 1 Kg", "1 Kg of high-quality caster sugar", LocalTime.parse("21:16:24"), 150L, 109L, "Grocery Haven", 3009L),
                new ProductInventory(20983L, 42L, 143L, 62L, "Pearl Barley 500g", "500g of pearl barley", LocalTime.parse("20:12:21"), 150L, 103L, "MegaMart", 3028L)
        ));

        // test
        Optional<List<ProductInventory>> result = repository.findPopularProductsInventory(null, LocalTime.parse("21:45:00"));
        assertEquals(expected, result);
    }

    @Test
    public void whenFindPopularProductsInventory_andPostcode_thenReturnPostcodesPopularProductsInventory() {
        // expected
        Optional<List<ProductInventory>> expected = Optional.of(Arrays.asList(
                new ProductInventory(290L, 3L, 5L, 13L, "Naval Oranges Per Kg", "Tangy and juicy oranges", LocalTime.parse("10:57:41"), 300L, 63L, "QuickGrocery", 3000L),
                new ProductInventory(302L, 6L, 2L, 16L, "Strawberries Punnet 300g", "Plump and juicy strawberries", LocalTime.parse("11:47:11"), 160L, 78L, "Grocery Haven", 3000L),
                new ProductInventory(292L, 4L, 2L, 14L, "Green Grapes Bag", "Seedless and succulent green grapes", LocalTime.parse("17:56:41"), 1460L, 181L, "Grocery Haven", 3000L),
                new ProductInventory(5L, 1L, 5L, 11L, "Cavendish Bananas Per Kg", "Ripe and fresh bananas", LocalTime.parse("17:49:33"), 400L, 165L, "QuickGrocery", 3000L),
                new ProductInventory(283L, 2L, 3L, 12L, "Jazz Apples Per Kg", "Crisp and sweet red apples", LocalTime.parse("19:58:59"), 400L, 72L, "MegaMart", 3000L),
                new ProductInventory(299L, 5L, 4L, 15L, "Watermelon Per Kg", "Sweet and red seedless watermelon", LocalTime.parse("15:29:18"), 460L, 160L, "FreshGrocer", 3000L),
                new ProductInventory(464L, 38L, 4L, 58L, "Plain Flour 1 Kg", "1 Kg of fine plain flour", LocalTime.parse("01:54:31"), 100L, 170L, "FreshGrocer", 3000L),
                new ProductInventory(136L, 28L, 1L, 46L, "1L Full Cream Milk", "1L full cream milk", LocalTime.parse("20:18:52"), 135L, 178L, "Supermart", 3000L),
                new ProductInventory(208L, 42L, 3L, 62L, "Pearl Barley 500g", "500g of pearl barley", LocalTime.parse("19:10:02"), 155L, 188L, "MegaMart", 3000L),
                new ProductInventory(470L, 39L, 5L, 59L, "Caster Sugar 1 Kg", "1 Kg of high-quality caster sugar", LocalTime.parse("11:29:59"), 170L, 35L, "QuickGrocery", 3000L)
        ));

        // test
        Optional<List<ProductInventory>> result = repository.findPopularProductsInventory(3000L, LocalTime.parse("21:45:00"));
        assertEquals(expected, result);
    }

    /**
     * Inventory Changes
     */
    @Test
    public void whenFindChanges_andNoChanges_thenReturnEmpty() {
        assertEquals(0 ,repository.findChange(LocalTime.parse("00:00:28"), LocalTime.parse("00:00:29")).size());
    }
    @Test
    public void whenFindChanges_andChanges_thenReturnChange() {
        List<ProductInventory> expected = List.of(
                new ProductInventory(8214L, 16L, 244L, 29L, "Lamb Leg Per Kg", "Delicious lamb leg cuts", LocalTime.parse("22:36:01"), 1210L, 47L, "FreshGrocer", 3048L),
                new ProductInventory(14594L, 29L, 189L, 46L, "2L Full Cream Milk", "2L full cream milk", LocalTime.parse("22:36:03"), 325L, 21L, "FreshGrocer", 3037L),
                new ProductInventory(26644L, 53L, 359L, 79L, "Frozen Meat Pies 4 Pack", "4-pack of frozen meat pies", LocalTime.parse("22:36:04"), 550L, 6L, "FreshGrocer", 3071L),
                new ProductInventory(8918L, 17L, 453L, 30L, "Pork Loin Steaks 500g", "Tasty pork loin steaks", LocalTime.parse("22:36:06"), 900L, 152L, "MegaMart", 3090L),
                new ProductInventory(9164L, 18L, 204L, 31L, "Tasmanian Salmon Per Kg", "Premium Tasmanian Atlantic salmon", LocalTime.parse("22:36:06"), 4500L, 57L, "FreshGrocer", 3040L),
                new ProductInventory(26160L, 52L, 370L, 78L, "Pork Dumplings 1 Kg", "1 Kg of tasty pork dumplings", LocalTime.parse("22:36:08"), 1170L, 52L, "QuickGrocery", 3073L),
                new ProductInventory(8961L, 17L, 496L, 30L, "Pork Loin Steaks 500g", "Tasty pork loin steaks", LocalTime.parse("22:36:09"), 860L, 200L, "Supermart", 3099L),
                new ProductInventory(9529L, 19L, 74L, 32L, "Small Rock Lobster", "Cooked and thawed small Rock lobster", LocalTime.parse("22:36:15"), 3050L, 26L, "FreshGrocer", 3014L),
                new ProductInventory(21112L, 42L, 272L, 62L, "Pearl Barley 500g", "500g of pearl barley", LocalTime.parse("22:36:20"), 175L, 94L, "Grocery Haven", 3054L),
                new ProductInventory(7251L, 14L, 271L, 27L, "Turkey Breast Per Kg", "Lean and flavorful turkey breast", LocalTime.parse("22:36:21"), 2005L, 158L, "Supermart", 3054L),
                new ProductInventory(20191L, 40L, 341L, 60L, "Penne Pasta 500g", "500g of penne pasta", LocalTime.parse("22:36:23"), 235L, 59L, "Supermart", 3068L),
                new ProductInventory(3120L, 6L, 100L, 16L, "Strawberries Punnet 300g", "Plump and juicy strawberries", LocalTime.parse("22:36:27"), 240L, 98L, "QuickGrocery", 3019L),
                new ProductInventory(3611L, 7L, 96L, 17L, "White Potatoes Per Kg", "White washed potatoes", LocalTime.parse("22:36:28"), 345L, 196L, "Supermart", 3019L)
                );
        assertEquals(expected, repository.findChange(LocalTime.parse("22:36:00"), LocalTime.parse("22:36:30")));
        assertEquals(13 ,repository.findChange(LocalTime.parse("22:36:00"), LocalTime.parse("22:36:30")).size());
    }
}
