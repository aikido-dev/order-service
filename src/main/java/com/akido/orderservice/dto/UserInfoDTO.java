package com.akido.orderservice.dto;

import com.akido.orderservice.enums.Role;

import java.util.UUID;

public record UserInfoDTO(
        UUID id,
        String username,
        Role role
) {
}
