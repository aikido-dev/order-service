package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.CurrentUserResponseDTO;
import com.akido.orderservice.dto.LoginRequestDTO;
import com.akido.orderservice.dto.LoginResponseDTO;
import com.akido.orderservice.dto.RegisterRequestDTO;
import com.akido.orderservice.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Зарегистрировать пользователя.",
            description = "Создает нового пользователя с ролью USER. " +
                    "Имя пользователя должно быть уникальным. " +
                    "Доступен всем."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно зарегистрирован."
    )
    @ApiResponse(
            responseCode = "409",
            description = "Пользователь с таким именем уже существует"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Имя пользователя или пароль не прошли валидацию."
    )
    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        authService.registerUser(registerDTO.username(), registerDTO.password());
    }

    @Operation(
            summary = "Аутентифицировать пользователя",
            description = "Проводит аутентификацию по имени пользователя и паролю. " +
                    "Возвращает JWT-токен. " +
                    "Доступен всем."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Аутентификация успешно пройдена."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Имя пользователя или пароль не прошли валидацию."
    )
    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponseDTO> loginUser(
            @RequestBody @Valid LoginRequestDTO loginDTO){
        String token = authService.loginUser(loginDTO.username(), loginDTO.password());

        if (token != null) {
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @Operation(
            summary = "Получить информацию о текущем пользователе.",
            description = "Возвращает имя и роль текущего пользователя. " +
                    "Доступен только аутентифицированным пользователям.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Информация о текущем пользователе успешно получена."
    )
    @GetMapping("/api/auth/me")
    public ResponseEntity<CurrentUserResponseDTO> currentUserInfo(
            @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(authService.getCurrentUserInfo(jwt));
    }
}
