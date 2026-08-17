package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Role;

public record CurrentUserDTO(
        String username,
        Role role
) {
}
