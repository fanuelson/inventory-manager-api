package com.example.demo.application.port.in;

import com.example.demo.domain.IdempotencyResult;
import com.example.demo.domain.Inventory;

public interface InventoryUseCase {
  IdempotencyResult<Inventory> decrease(String idempotencyKey, String id, long total);
}
