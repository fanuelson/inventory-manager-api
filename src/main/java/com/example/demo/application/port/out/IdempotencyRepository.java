package com.example.demo.application.port.out;

import com.example.demo.domain.IdempotencyRecord;

import java.util.Optional;

public interface IdempotencyRepository {
  boolean tryInsert(String key, String requestHash);
  Optional<IdempotencyRecord> findByKey(String key);
  void saveResponse(String key, String body);
}
