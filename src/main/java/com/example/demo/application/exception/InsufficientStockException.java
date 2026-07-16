package com.example.demo.application.exception;

public class InsufficientStockException extends RuntimeException {
  public InsufficientStockException(String id, long available, long requested) {
    super("Insufficient stock for inventory '%s': available=%d, requested=%d"
        .formatted(id, available, requested));
  }
}
