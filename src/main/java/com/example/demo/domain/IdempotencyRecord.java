package com.example.demo.domain;

public record IdempotencyRecord(
    String key,
    String requestHash,
    String resultBody
) {
}
