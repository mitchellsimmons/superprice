package com.scrumdogs.superprice.product;
import com.scrumdogs.superprice.product.controllers.ProductNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor // Performs @Autowiring if only one constructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public Collection<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Optional<List<Product>> getProductsByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Product> getProductById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<List<ProductInventory>> getProductInventoryById(Long id) {
        return repository.findInventoryById(id, LocalTime.now().withNano(0));
    }

    @Override
    public Optional<List<ProductInventory>> getProductInventoryByIdAndPostcode(Long id, Long postcode) {
        return repository.findInventoryByIdAndPostcode(id, postcode, LocalTime.now().withNano(0));
    }
    @Override
    public Optional<List<ProductInventory>> getProductInventoryByName(String name) {
        return repository.findInventoryByName(name, LocalTime.now().withNano(0));
    }
    @Override
    public Optional<List<ProductInventory>> getProductInventoryByNameAndPostcode(String name, Long postcode) {
        return repository.findInventoryByNameAndPostcode(name, postcode, LocalTime.now().withNano(0));
    }

    @Override
    public Optional<List<Product>> getProductsByCategory(Long categoryId) {
        return repository.findByCategory(categoryId);
    }

    @Override
    public Optional<List<ProductInventory>> getPopularProductsInventory(Long postcode) {
        return repository.findPopularProductsInventory(postcode, LocalTime.now().withNano(0));
    }

    @Override
    public List<ProductInventory> getPriceChanges(LocalTime startTime, LocalTime endTime) {
        return repository.findChange(startTime, endTime);
    }

}