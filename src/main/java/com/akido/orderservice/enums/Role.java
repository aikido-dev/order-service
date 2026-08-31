package com.akido.orderservice.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    USER, ADMIN;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }
}
