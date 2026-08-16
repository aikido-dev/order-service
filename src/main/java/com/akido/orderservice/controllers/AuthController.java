package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.LoginRequestDTO;
import com.akido.orderservice.dto.LoginResponseDTO;
import com.akido.orderservice.dto.RegisterRequestDTO;
import com.akido.orderservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public void registerUser(@RequestBody RegisterRequestDTO registerDTO) {
        authService.registerUser(registerDTO.username(), registerDTO.password());
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponseDTO> loginUser(
            @RequestBody LoginRequestDTO loginDTO){
        String token = authService.loginUser(loginDTO.username(), loginDTO.password());

        if (token != null) {
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
