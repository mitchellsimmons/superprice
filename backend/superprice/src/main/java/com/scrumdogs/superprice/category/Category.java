package com.scrumdogs.superprice.category;

import java.util.List;

public record Category(Long id, String name, Long parentID, List<Category> children) {

}