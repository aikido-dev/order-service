package com.akido.orderservice.services;

import com.akido.orderservice.dto.UserResponseDTO;
import com.akido.orderservice.mappers.UserMapper;
import com.akido.orderservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();

    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }
}
