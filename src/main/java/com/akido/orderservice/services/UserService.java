package com.akido.orderservice.services;

import com.akido.orderservice.dto.UserResponseDTO;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserResponseDTO> result = new ArrayList<>();

        for (User user : allUsers) {
            result.add(new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getRole()));
        }
        return result;
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }
}
