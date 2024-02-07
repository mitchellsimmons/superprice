package com.scrumdogs.superprice.category;

import com.scrumdogs.superprice.category.controllers.CategoryController;
import com.scrumdogs.superprice.category.controllers.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryControllerTest {

    private CategoryController controller;

    private CategoryService service;

    @BeforeEach
    void setup() {
        this.service = mock(CategoryService.class);
        this.controller = new CategoryController(this.service);
    }

    @Test
    void shouldReturnCompleteTreeWhenNoParentIDIsNull() {
        //  expected tree
        List<Category> expected = Arrays.asList(
            new Category(1L, "Fruit", null,  new ArrayList<>(Arrays.asList(
                new Category(2L, "Bananas", 1L, null),
                new Category(3L, "Apples", 1L, null)))),
            new Category(4L, "Dairy", null,  new ArrayList<>(Arrays.asList(
                new Category(5L, "Milk", 4L, null),
                new Category(6L, "Cream", 4L, null)))));

        when(service.getCategoryTree()).thenReturn(Optional.of(expected));

        //  test
        List<Category> result = controller.all();
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnSubtreeWhenValidParentID() throws CategoryNotFoundException {
        //  expected tree
        //  note we expect the given root to be included as well
        List<Category> expected = List.of(
                new Category(4L, "Dairy", null, new ArrayList<>(Arrays.asList(
                        new Category(5L, "Milk", 4L, null),
                        new Category(6L, "Cream", 4L, null)))));

        when(service.getCategorySubTree(4L)).thenReturn(Optional.of(expected));

        //  test
        List<Category> result = controller.root(4L);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnNothingWhenRepositoryEmpty() throws CategoryNotFoundException {
        //  note that we don't need the same test for controller.root,
        //  as it is handled by the invalid id test.

        //  expected empty tree
        List<Category> expected = List.of();

        //  mock
        when(service.getCategoryTree()).thenReturn(Optional.of(expected));

        //  test
        List<Category> result = controller.all();
        //  TODO: it looks like the DB returns an empty list, rather than null so the optional is never empty.
        // assertFalse(result.isPresent());
        assertEquals(expected, result);
    }

    @Test
    void shouldRaiseExceptionWhenInvalidParentID() {
        assertThrows(CategoryNotFoundException.class,
                () -> controller.root(19L));
    }
}

