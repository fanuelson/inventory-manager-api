package com.example.demo.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DecreaseInventoryRequest(
    @NotBlank String id,
    @Positive long total
) {
}
