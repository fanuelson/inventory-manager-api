package com.example.demo.application.exception;

public class InventoryNotFoundException extends RuntimeException {
  public InventoryNotFoundException(String id) {
    super("Inventory '%s' not found".formatted(id));
  }
}
