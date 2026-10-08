package com.akido.orderservice.services;

import com.akido.orderservice.dto.UserResponseDTO;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.enums.Role;
import com.akido.orderservice.mappers.UserMapper;
import com.akido.orderservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnMappedUsers(){
        // Arrange
        User user1 = new User();
        User user2 = new User();

        UserResponseDTO dto1 = new UserResponseDTO(UUID.randomUUID(), "user1", Role.USER);
        UserResponseDTO dto2 = new UserResponseDTO(UUID.randomUUID(), "user2", Role.USER);

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        when(userMapper.toDTO(user1))
                .thenReturn(dto1);

        when(userMapper.toDTO(user2))
                .thenReturn(dto2);

        // Act
        List<UserResponseDTO> result =
                userService.getAllUsers();

        // Assert
        assertEquals(List.of(dto1, dto2), result);

        verify(userRepository).findAll();
        verify(userMapper).toDTO(user1);
        verify(userMapper).toDTO(user2);
    }

    @Test
    void getAllUsers_whenUsersDoNotExist_shouldReturnEmptyList() {
        // Arrange
        when(userRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<UserResponseDTO> result =
                userService.getAllUsers();

        // Assert
        assertTrue(result.isEmpty());

        verify(userRepository).findAll();
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUserById_shouldDeleteUser(){
        UUID id = UUID.randomUUID();

        userService.deleteUserById(id);

        verify(userRepository).deleteById(id);
    }
}
