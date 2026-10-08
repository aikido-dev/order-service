package com.akido.orderservice.services;

import com.akido.orderservice.dto.CurrentUserResponseDTO;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.enums.Role;
import com.akido.orderservice.exceptions.UserAlreadyExistsException;
import com.akido.orderservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    JWTService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthService authService;

    @Test
    void getCurrentUserInfo_shouldReturnCurrentUserResponseDTO(){
        //Arrange
        Jwt jwt = mock(Jwt.class);

        when(jwt.getClaimAsString("role"))
                .thenReturn("USER");

        when(jwt.getSubject())
                .thenReturn("Petr");

        //Act
        CurrentUserResponseDTO userResponseDTO = authService.getCurrentUserInfo(jwt);

        //Assert
        assertEquals(new CurrentUserResponseDTO("Petr", Role.USER),userResponseDTO);
    }

    @Test
    void loginUser_shouldReturnJwt(){
        //Arrange
        String username = "Ivan";
        String password = "12345";

        String expectedToken = "expectedToken";
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(authentication);

        when(jwtService.createJWT(authentication))
                .thenReturn(expectedToken);

        //Act
        String token = authService.loginUser(username, password);
        //Assert
        assertEquals(expectedToken, token);
    }

    @Test
    void loginUser_whenCredentialsIsBad_shouldThrowException(){
        //Arrange
        String username = "Ivan";
        String password = "12345";


        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

        //Assert
        assertThrows(BadCredentialsException.class, () -> authService.loginUser(username, password));
        verify(jwtService, never()).createJWT(any());
    }

    @Test
    void registerUser_shouldSaveUser(){
        String username = "username";
        String password = "password";
        String encodedPassword = "encodedPassword";

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        when(userRepository.existsByUsername(username))
                .thenReturn(false);

        when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);

        authService.registerUser(username, password);

        verify(userRepository).save(captor.capture());
        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(password);

        User savedUser = captor.getValue();

        assertEquals(username, savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

    }

    @Test
    void registerUser_whenUsernameExists_shouldThrowException(){
        String username = "username";
        String password = "password";

        when(userRepository.existsByUsername(username))
                .thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(username, password));

        verify(userRepository, never()).save(any());
    }
}
