package com.scrumdogs.superprice.product;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.List;
import java.util.Collection;
import java.util.Optional;


public interface ProductService {
    Collection<Product> getAllProducts();

    Optional<Product> getProductById(long id);

    Optional<List<Product>> getProductsByName(String name);

    Optional<List<Product>> getProductsByCategory(Long categoryId);

    Optional<List<ProductInventory>> getProductInventoryById(Long id);

    Optional<List<ProductInventory>> getProductInventoryByIdAndPostcode(Long id, Long postcode);

    Optional<List<ProductInventory>> getProductInventoryByName(String name);

    Optional<List<ProductInventory>> getProductInventoryByNameAndPostcode(String name, Long postcode);

    Optional<List<ProductInventory>> getPopularProductsInventory(Long postcode);

    List<ProductInventory> getPriceChanges(LocalTime startTime, LocalTime endTime);
}