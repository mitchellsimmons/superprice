package com.scrumdogs.superprice.category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<List<Category>> findFullCategoryTree();
    Optional<List<Category>> findCategoryTreeFromParent(Long parentCategoryID);
}

