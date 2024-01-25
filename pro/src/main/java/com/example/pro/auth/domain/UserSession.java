package com.example.pro.auth.domain;

import com.example.pro.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id @NotNull
    private String sessionId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime expiredAt;
    public static final String SESSION_KEY = "JSESSIONID";
    public static final int SESSION_EXPIRED_SECOND = 60; // TODO: 테스트를 위해 1분으로 설정. 배포전 수정 필요.

    public static Session create(String sessionId, String username) {
        return new Session(sessionId, username);
    }

    private Session(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.createdAt = LocalDateTime.now();
        this.lastAccessedAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusSeconds(SESSION_EXPIRED_SECOND);
    }

    public boolean isValidate() {
        return this.expiredAt.isBefore(LocalDateTime.now());
    }
}
