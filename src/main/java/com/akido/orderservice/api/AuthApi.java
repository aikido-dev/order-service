package com.akido.orderservice.api;

import com.akido.orderservice.dto.CurrentUserResponseDTO;
import com.akido.orderservice.dto.LoginRequestDTO;
import com.akido.orderservice.dto.LoginResponseDTO;
import com.akido.orderservice.dto.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthApi {

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
    void registerUser(RegisterRequestDTO registerDTO);

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
    ResponseEntity<LoginResponseDTO> loginUser(LoginRequestDTO loginDTO);

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
    ResponseEntity<CurrentUserResponseDTO> currentUserInfo(Jwt jwt);
}
