package com.akido.orderservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDTO(
        @NotBlank String description
) {
}
