package com.scrumdogs.superprice.category;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setup() {
        flyway.migrate();
        repository = new CategoryRepositoryImpl(dataSource);
    }

    @AfterEach
    public void teardown() {
        flyway.clean();
    }

    @Test
    public void findCategoryTreeFromParent_should_returnFullCategoryTree_when_nodeIsNull() {
        //  expected results
        Category expectedSubTree =
                new Category(1L, "Fruit & Vegetables", 0L,  new ArrayList<>(Arrays.asList(
                        new Category(8L, "Fruit", 1L, new ArrayList<>(Arrays.asList(
                            new Category(11L, "Bananas", 8L, new ArrayList<>()),
                            new Category(12L, "Apples", 8L, new ArrayList<>()),
                            new Category(13L, "Citrus", 8L, new ArrayList<>()),
                            new Category(14L, "Grapes", 8L, new ArrayList<>()),
                            new Category(15L, "Melons", 8L, new ArrayList<>()),
                            new Category(16L, "Berries", 8L, new ArrayList<>())
                        ))),
                        new Category(9L, "Vegetables", 1L, new ArrayList<>(Arrays.asList(
                            new Category(17L, "Potatoes & Pumpkins", 9L, new ArrayList<>()),
                            new Category(18L, "Carrots & Roots", 9L, new ArrayList<>()),
                            new Category(19L, "Mushrooms", 9L, new ArrayList<>()),
                            new Category(20L, "Tomatoes", 9L, new ArrayList<>())
                        ))),
                        new Category(10L, "Salad", 1L, new ArrayList<>(Arrays.asList(
                            new Category(21L, "Herbs", 10L, new ArrayList<>()),
                            new Category(22L, "Salad Bags", 10L, new ArrayList<>())
                        )))
                )));

        //  test
        Optional<List<Category>> categoryTree = repository.findCategoryTreeFromParent(null);
        // Test Fruit & Vegetables subtree
        assertEquals(expectedSubTree, categoryTree.get().get(0));
        // Test number of categories in each subtree
        assertEquals(7, categoryTree.get().size());
        assertEquals(3, categoryTree.get().get(0).children().size());
        assertEquals(6, categoryTree.get().get(0).children().get(0).children().size());
        assertEquals(4, categoryTree.get().get(0).children().get(1).children().size());
        assertEquals(2, categoryTree.get().get(0).children().get(2).children().size());
        assertEquals(3, categoryTree.get().get(1).children().size());
        assertEquals(2, categoryTree.get().get(1).children().get(0).children().size());
        assertEquals(3, categoryTree.get().get(1).children().get(1).children().size());
        assertEquals(4, categoryTree.get().get(1).children().get(2).children().size());
        assertEquals(2, categoryTree.get().get(2).children().size());
        assertEquals(3, categoryTree.get().get(2).children().get(0).children().size());
        assertEquals(3, categoryTree.get().get(2).children().get(1).children().size());
        assertEquals(3, categoryTree.get().get(3).children().size());
        assertEquals(3, categoryTree.get().get(3).children().get(0).children().size());
        assertEquals(2, categoryTree.get().get(3).children().get(1).children().size());
        assertEquals(2, categoryTree.get().get(3).children().get(2).children().size());
        assertEquals(3, categoryTree.get().get(4).children().size());
        assertEquals(2, categoryTree.get().get(4).children().get(0).children().size());
        assertEquals(2, categoryTree.get().get(4).children().get(1).children().size());
        assertEquals(3, categoryTree.get().get(4).children().get(2).children().size());
        assertEquals(3, categoryTree.get().get(5).children().size());
        assertEquals(2, categoryTree.get().get(5).children().get(0).children().size());
        assertEquals(2, categoryTree.get().get(5).children().get(1).children().size());
        assertEquals(2, categoryTree.get().get(5).children().get(2).children().size());
        assertEquals(3, categoryTree.get().get(6).children().size());
        assertEquals(2, categoryTree.get().get(6).children().get(0).children().size());
        assertEquals(3, categoryTree.get().get(6).children().get(1).children().size());
        assertEquals(2, categoryTree.get().get(6).children().get(2).children().size());
    }

    @Test
    public void findCategoryTreeFromParent_should_returnSubTree_when_nodeIsNotRoot() {
        //  expected results
        Optional<List<Category>> expected = Optional.of(List.of(
                new Category(1L, "Fruit & Vegetables", 0L,  new ArrayList<>(Arrays.asList(
                        new Category(8L, "Fruit", 1L, new ArrayList<>(Arrays.asList(
                            new Category(11L, "Bananas", 8L, new ArrayList<>()),
                            new Category(12L, "Apples", 8L, new ArrayList<>()),
                            new Category(13L, "Citrus", 8L, new ArrayList<>()),
                            new Category(14L, "Grapes", 8L, new ArrayList<>()),
                            new Category(15L, "Melons", 8L, new ArrayList<>()),
                            new Category(16L, "Berries", 8L, new ArrayList<>())
                        ))),
                        new Category(9L, "Vegetables", 1L, new ArrayList<>(Arrays.asList(
                            new Category(17L, "Potatoes & Pumpkins", 9L, new ArrayList<>()),
                            new Category(18L, "Carrots & Roots", 9L, new ArrayList<>()),
                            new Category(19L, "Mushrooms", 9L, new ArrayList<>()),
                            new Category(20L, "Tomatoes", 9L, new ArrayList<>())
                        ))),
                        new Category(10L, "Salad", 1L, new ArrayList<>(Arrays.asList(
                            new Category(21L, "Herbs", 10L, new ArrayList<>()),
                            new Category(22L, "Salad Bags", 10L, new ArrayList<>())
                        )))
                )))
        ));

        //  test
        assertEquals(expected, repository.findCategoryTreeFromParent(1L));
    }

    @Test
    public void findCategoryTreeFromParent_should_returnNull_when_categoryIdDoesNotExist() {
        Optional<List<Category>> actual = repository.findCategoryTreeFromParent(10000000L);
        assertFalse(actual.isPresent());
    }
}
