package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.UserInfoDTO;
import com.akido.orderservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public List<UserInfoDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
