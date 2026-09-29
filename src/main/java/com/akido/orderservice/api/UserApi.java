package com.akido.orderservice.api;

import com.akido.orderservice.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
public interface UserApi {

    @Operation(
            summary = "Получить всех пользователей.",
            description = "Возвращает список всех пользователей. " +
                    "Доступен только администраторам."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "403",
            description = "Недостаточно прав для этой операции."
    )
    List<UserResponseDTO> getAllUsers();

    @Operation(
            summary = "Удалить пользователя.",
            description = "Удаляет пользователя по id. " +
                    "Доступен только администраторам."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Пользователь успешно удален."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "403",
            description = "Недостаточно прав для этой операции."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Идентификатор пользователя не прошел валидацию."
    )
    void deleteUserById(UUID id);
}
