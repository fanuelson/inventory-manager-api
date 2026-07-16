package com.example.demo.application.exception;

public class IdempotencyKeyReusedException extends RuntimeException {
  public IdempotencyKeyReusedException(String key) {
    super("Idempotency key '%s' was already used with a different request payload".formatted(key));
  }
}
