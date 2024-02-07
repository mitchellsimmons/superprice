package com.scrumdogs.superprice.product;

import java.util.List;
import java.util.Optional;
import java.time.LocalTime;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(long id);

    Optional<List<Product>> findByName(String name);

    Optional<List<Product>> findByCategory(Long categoryId);

    Optional<List<ProductInventory>> findPopularProductsInventory(Long postcode, LocalTime time);

    Optional<List<ProductInventory>> findInventoryById(Long id, LocalTime time);

    Optional<List<ProductInventory>> findInventoryByIdAndPostcode(Long id, Long postcode, LocalTime time);

    Optional<List<ProductInventory>> findInventoryByName(String name, LocalTime time);

    Optional<List<ProductInventory>> findInventoryByNameAndPostcode(String name, Long Postcode, LocalTime time);

    List<ProductInventory> findChange(LocalTime startTime, LocalTime endTime);

}