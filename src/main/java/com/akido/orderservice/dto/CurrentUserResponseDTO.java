package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Role;

public record CurrentUserResponseDTO(
        String username,
        Role role
) {
}
