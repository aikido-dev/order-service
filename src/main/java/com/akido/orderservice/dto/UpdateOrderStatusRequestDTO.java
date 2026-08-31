package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull Status status
) {
}
