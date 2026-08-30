package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.UserResponseDTO;
import com.akido.orderservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    @GetMapping("/api/users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/users/{id}")
    public void deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
    }
}
