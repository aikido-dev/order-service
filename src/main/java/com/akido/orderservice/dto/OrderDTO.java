package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        UUID userId,
        String description,
        Status status,
        LocalDateTime createdAt
) {
}
