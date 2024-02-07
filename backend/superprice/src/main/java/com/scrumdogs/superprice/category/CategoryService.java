package com.scrumdogs.superprice.category;

import com.scrumdogs.superprice.category.controllers.CategoryNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<List<Category>> getCategoryTree();
    Optional<List<Category>> getCategorySubTree(Long rootCategoryId);
}