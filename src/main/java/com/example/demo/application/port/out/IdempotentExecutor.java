package com.example.demo.application.port.out;

import com.example.demo.domain.IdempotencyResult;

import java.util.function.Supplier;

public interface IdempotentExecutor {
  <T> IdempotencyResult<T> execute(
      String key,
      Object request,
      Class<T> resultType,
      Supplier<T> operation);
}
