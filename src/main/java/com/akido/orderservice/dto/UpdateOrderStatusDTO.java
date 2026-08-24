package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusDTO(@NotNull Status status) {
}
