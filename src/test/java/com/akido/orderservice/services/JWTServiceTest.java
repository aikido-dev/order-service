package com.akido.orderservice.services;

import com.akido.orderservice.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {
    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private JWTService jwtService;

    @Test
    void createJWT_shouldReturnToken() {
        // Arrange
        String expectedToken = "jwt-encoded-token";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "Ivan",
                        null,
                        List.of(Role.USER.toAuthority())
                );

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor
                .forClass(JwtEncoderParameters.class);

        Jwt jwt = mock(Jwt.class);

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        when(jwt.getTokenValue())
                .thenReturn(expectedToken);

        // Act
        String token = jwtService.createJWT(authentication);

        // Assert
        assertEquals(expectedToken, token);
        verify(jwtEncoder).encode(captor.capture());

        JwtEncoderParameters params = captor.getValue();
        JwtClaimsSet claims = params.getClaims();

        assertEquals("Ivan", claims.getSubject());
        assertEquals(Role.USER, Role.valueOf(claims.getClaim("role")));
    }
}