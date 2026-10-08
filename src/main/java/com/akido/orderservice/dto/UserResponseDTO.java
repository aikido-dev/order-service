package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Role;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        Role role
) {
}
