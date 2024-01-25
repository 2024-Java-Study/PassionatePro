package com.example.pro.auth;

import org.springframework.security.core.Authentication;

public interface AuthProvider {
    Authentication getAuthentication(String id);
}
