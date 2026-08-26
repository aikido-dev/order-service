package com.akido.orderservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
