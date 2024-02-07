package com.scrumdogs.superprice.category;

import com.scrumdogs.superprice.category.controllers.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public Optional<List<Category>> getCategoryTree() {
        return repository.findFullCategoryTree();
    }
        @Override
    public Optional<List<Category>> getCategorySubTree(Long rootCategoryId) {
        return repository.findCategoryTreeFromParent(rootCategoryId);
    }
}