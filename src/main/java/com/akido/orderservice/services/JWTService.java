package com.akido.orderservice.services;

import com.akido.orderservice.entities.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {
    private final JwtEncoder jwtEncoder;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String getJWT(User user){
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .claim("role", user.getRole().name())
                .build();

        JwtEncoderParameters params = JwtEncoderParameters.from(claimsSet);

        return jwtEncoder.encode(params).getTokenValue();
    }
}
