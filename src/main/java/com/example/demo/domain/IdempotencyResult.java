package com.example.demo.domain;

public record IdempotencyResult<T>(T value, boolean replayed) {

  public static <T> IdempotencyResult<T> executed(T value) {
    return new IdempotencyResult<>(value, false);
  }

  public static <T> IdempotencyResult<T> replayed(T value) {
    return new IdempotencyResult<>(value, true);
  }
}