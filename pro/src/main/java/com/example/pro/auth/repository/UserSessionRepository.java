package com.example.pro.auth;

import com.example.pro.auth.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
}
