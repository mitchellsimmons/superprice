package com.scrumdogs.superprice.category;

import com.scrumdogs.superprice.category.controllers.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryService service;
    private final CategoryRepository repository = mock(CategoryRepository.class);

    @BeforeEach
    void setup() {
        this.service = new CategoryServiceImpl(repository);
    }

    @Test
    public void shouldReturnSubTree() {
        //  expected
        List<Category> expected = List.of(
                new Category(4L, "Dairy", null,  new ArrayList<>(Arrays.asList(
                        new Category(5L, "Milk", 4L, null),
                        new Category(6L, "Cream", 4L, null)))));

        when(repository.findCategoryTreeFromParent(4L)).thenReturn(Optional.of(expected));

        //  test
        Optional<List<Category>> result = service.getCategorySubTree(4L);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void shouldReturnFullTree() {
        // expected
        Optional<List<Category>> expected = Optional.of(Arrays.asList(
                new Category(1L, "Fruit", null,  new ArrayList<>(Arrays.asList(
                        new Category(2L, "Bananas", 1L, null),
                        new Category(3L, "Apples", 1L, null)))),
                new Category(4L, "Dairy", null,  new ArrayList<>(Arrays.asList(
                        new Category(5L, "Milk", 4L, null),
                        new Category(6L, "Cream", 4L, null))))));

        // mock
        when(repository.findFullCategoryTree()).thenReturn(expected);

        // test
        Optional<List<Category>> result = service.getCategoryTree();
        assertTrue(result.isPresent());
        assertEquals(expected, result);
    }

    @Test
    public void whenNoCategoriesExistShouldReturn() {
        // expected
        Optional expected = Optional.empty();

        // mock
        when(repository.findFullCategoryTree()).thenReturn(Optional.empty());

        // test
        assertEquals(expected, service.getCategoryTree());
        assertEquals(expected, service.getCategorySubTree(4L));
    }

}
