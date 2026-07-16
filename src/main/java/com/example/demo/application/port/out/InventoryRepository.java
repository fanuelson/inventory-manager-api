package com.example.demo.application.port.out;

import com.example.demo.domain.Inventory;

import java.util.Optional;

public interface InventoryRepository {
  boolean decrease(String id, long total);
  Optional<Inventory> findById(String id);
}
