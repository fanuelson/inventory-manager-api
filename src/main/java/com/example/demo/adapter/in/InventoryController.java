package com.example.demo.adapter.in;

import com.example.demo.adapter.in.request.DecreaseInventoryRequest;
import com.example.demo.application.port.in.InventoryUseCase;
import com.example.demo.domain.Inventory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryUseCase inventoryUseCase;

  @PostMapping
  public ResponseEntity<Inventory> decrease(
      @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
      @RequestBody @Valid DecreaseInventoryRequest request) {

    final var result = inventoryUseCase.decrease(idempotencyKey, request.id(), request.total());

    return ResponseEntity.ok()
        .header("Idempotency-Replayed", String.valueOf(result.replayed()))
        .body(result.value());
  }
}
