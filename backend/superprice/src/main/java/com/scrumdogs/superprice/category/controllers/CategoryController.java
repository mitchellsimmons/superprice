package com.scrumdogs.superprice.category.controllers;

import com.scrumdogs.superprice.category.Category;
import com.scrumdogs.superprice.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class CategoryController {
    final private CategoryService categoryService;

    @GetMapping("v1/categories")
    @ResponseBody
    public List<Category> all() {
        return categoryService.getCategoryTree().orElseThrow(
                () -> new CategoryNotFoundException(0L));
    }

    @RequestMapping(value = "/v1/categories", params = "root", method = GET)
    @ResponseBody
    public List<Category> root(@RequestParam("root")  Long rootCategoryId) throws CategoryNotFoundException {
        return categoryService.getCategorySubTree(rootCategoryId).orElseThrow(
                () -> new CategoryNotFoundException(rootCategoryId));
    }
}