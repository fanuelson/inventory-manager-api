package com.example.demo.adapter.out;

import com.example.demo.application.exception.IdempotencyInProgressException;
import com.example.demo.application.exception.IdempotencyKeyReusedException;
import com.example.demo.application.port.out.IdempotentExecutor;
import com.example.demo.domain.IdempotencyResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class IdempotentExecutorJdbc implements IdempotentExecutor {

  private final IdempotencyJdbcRepository repository;
  private final ObjectMapper objectMapper;

  @Transactional
  public <T> IdempotencyResult<T> execute(
      String key,
      Object request,
      Class<T> resultType,
      Supplier<T> operation) {

    var requestHash = sha256(request);

    if (!repository.tryInsert(key, requestHash)) {
      return replay(key, requestHash, resultType);
    }

    var result = operation.get();
    repository.saveResponse(key, objectMapper.writeValueAsString(result));
    return IdempotencyResult.executed(result);
  }

  private <T> IdempotencyResult<T> replay(String key, String requestHash, Class<T> resultType) {
    var existing = repository.findByKey(key)
        .orElseThrow(() -> new IdempotencyInProgressException(key));
    if (!existing.requestHash().equals(requestHash)) {
      throw new IdempotencyKeyReusedException(key);
    }
    if (existing.resultBody() == null) {
      throw new IdempotencyInProgressException(key);
    }
    return IdempotencyResult.replayed(objectMapper.readValue(existing.resultBody(), resultType));
  }

  private String sha256(Object request) {
    try {
      var digest = MessageDigest.getInstance("SHA-256")
          .digest(objectMapper.writeValueAsBytes(request));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
