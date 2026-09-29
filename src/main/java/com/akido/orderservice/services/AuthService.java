package com.akido.orderservice.services;

import com.akido.orderservice.dto.CurrentUserResponseDTO;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.enums.Role;
import com.akido.orderservice.exceptions.UserAlreadyExistsException;
import com.akido.orderservice.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void registerUser(String username, String password) {
        if(userRepository.existsByUsername(username)){
            throw new UserAlreadyExistsException(username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return jwtService.createJWT(authentication);
    }

    public CurrentUserResponseDTO getCurrentUserInfo(Jwt jwt) {
        String role = jwt.getClaimAsString("role");
        String username = jwt.getSubject();

        return new CurrentUserResponseDTO(username, Role.valueOf(role));
    }
}
