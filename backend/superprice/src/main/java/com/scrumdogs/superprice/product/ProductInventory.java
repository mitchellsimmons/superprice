package com.scrumdogs.superprice.product;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ProductInventory(Long inventoryId, Long productId, Long storeId, Long categoryId, String name, String description, LocalTime time, Long priceInCents, Long stockLevel, String storeName, Long postcode) {}
