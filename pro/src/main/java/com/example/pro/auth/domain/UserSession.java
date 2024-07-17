package com.example.pro.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSession {
    @Id @NotNull
    private String sessionId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime expiredAt;
    public static final String SESSION_KEY = "JSESSIONID";
    public static final int SESSION_EXPIRED_SECOND = 30 * 60; // 30분

    public static UserSession create(String sessionId, String username, Clock clock) {
        return new UserSession(sessionId, username, clock);
    }
    private UserSession(String sessionId, String username, Clock clock) {
        this.sessionId = sessionId;
        this.username = username;
        this.createdAt = LocalDateTime.now(clock);
        this.lastAccessedAt = this.createdAt;
        this.expiredAt = this.createdAt.plusSeconds(SESSION_EXPIRED_SECOND);
    }

    public UserSession update(Clock clock) {
        this.lastAccessedAt = LocalDateTime.now(clock);
        this.expiredAt = this.lastAccessedAt.plusSeconds(SESSION_EXPIRED_SECOND);
        return this;
    }

    public boolean isValidate(Clock clock) {
        return this.expiredAt.isAfter(LocalDateTime.now(clock));
    }

}
