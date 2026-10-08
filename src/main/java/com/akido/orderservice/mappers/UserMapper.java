package com.akido.orderservice.mappers;

import com.akido.orderservice.dto.UserResponseDTO;
import com.akido.orderservice.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole());
    }
}
