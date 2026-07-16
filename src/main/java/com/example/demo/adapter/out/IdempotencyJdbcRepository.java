package com.example.demo.adapter.out;

import com.example.demo.application.port.out.IdempotencyRepository;
import com.example.demo.domain.IdempotencyRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IdempotencyJdbcRepository implements IdempotencyRepository {

  private final JdbcClient jdbc;

  public boolean tryInsert(String key, String requestHash) {
    return jdbc.sql("""
            INSERT INTO idempotency_keys (idempotency_key, request_hash)
            VALUES (:key, :requestHash)
            ON CONFLICT DO NOTHING
            """)
        .param("key", key)
        .param("requestHash", requestHash)
        .update() == 1;
  }

  public Optional<IdempotencyRecord> findByKey(String key) {
    return jdbc.sql("""
            SELECT idempotency_key, request_hash, result_body
            FROM idempotency_keys
            WHERE idempotency_key = :key
            """)
        .param("key", key)
        .query((rs, rowNum) -> new IdempotencyRecord(
            rs.getString("idempotency_key"),
            rs.getString("request_hash"),
            rs.getString("result_body")))
        .optional();
  }

  public void saveResponse(String key, String body) {
    jdbc.sql("""
            UPDATE idempotency_keys
            SET result_body = :body
            WHERE idempotency_key = :key
            """)
        .param("key", key)
        .param("body", body)
        .update();
  }
}
