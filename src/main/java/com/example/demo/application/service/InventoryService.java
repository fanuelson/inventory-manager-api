package com.example.demo.application.service;

import com.example.demo.application.exception.InsufficientStockException;
import com.example.demo.application.exception.InventoryNotFoundException;
import com.example.demo.application.port.in.InventoryUseCase;
import com.example.demo.application.port.out.IdempotentExecutor;
import com.example.demo.application.port.out.InventoryRepository;
import com.example.demo.domain.IdempotencyResult;
import com.example.demo.domain.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements InventoryUseCase {

  private final IdempotentExecutor idempotentExecutor;
  private final InventoryRepository inventoryRepository;

  public IdempotencyResult<Inventory> decrease(String idempotencyKey, String id, long total) {
    return idempotentExecutor.execute(
        idempotencyKey, new DecreaseCommand(id, total), Inventory.class,
        () -> {
          if (!inventoryRepository.decrease(id, total)) {
            var inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));
            throw new InsufficientStockException(id, inventory.getTotalAvailable(), total);
          }
          return inventoryRepository.findById(id)
              .orElseThrow(() -> new InventoryNotFoundException(id));
        });
  }

  private record DecreaseCommand(String id, long total) {
  }
}
