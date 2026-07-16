package com.example.demo.application.exception;

public class IdempotencyInProgressException extends RuntimeException {
  public IdempotencyInProgressException(String key) {
    super("A request with idempotency key '%s' is still being processed".formatted(key));
  }
}
