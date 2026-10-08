package com.akido.orderservice.controllers;

import com.akido.orderservice.api.AuthApi;
import com.akido.orderservice.dto.CurrentUserResponseDTO;
import com.akido.orderservice.dto.LoginRequestDTO;
import com.akido.orderservice.dto.LoginResponseDTO;
import com.akido.orderservice.dto.RegisterRequestDTO;
import com.akido.orderservice.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        authService.registerUser(registerDTO.username(), registerDTO.password());
    }

    @PostMapping("/api/auth/login")
    @Override
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO loginDTO){
        String token = authService.loginUser(loginDTO.username(), loginDTO.password());

        if (token != null) {
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @Override
    @GetMapping("/api/auth/me")
    public ResponseEntity<CurrentUserResponseDTO> currentUserInfo(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(authService.getCurrentUserInfo(jwt));
    }
}
