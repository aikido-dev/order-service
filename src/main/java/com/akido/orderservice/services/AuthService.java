package com.akido.orderservice.services;

import com.akido.orderservice.entities.User;
import com.akido.orderservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    JWTService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
        this.jwtService = jwtService;
    }

    public void registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public String loginUser(String username, String password) {
       User user = userRepository.findByUsername(username);

       if (user != null && passwordEncoder.matches(password, user.getPassword())) {
           return jwtService.getJWT(user);
       }

       return null;
    }
}
