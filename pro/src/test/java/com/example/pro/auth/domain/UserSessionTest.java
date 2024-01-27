package com.example.pro.auth.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSessionTest {
    UserSession session;
    @Mock
    Clock clock;
    final String SESSION_ID = "randomSessionId";
    final String USERNAME = "username";
    final Instant CREATED = Instant.parse("2024-01-26T02:12:32.39Z");
    final Instant UPDATED = Instant.parse("2024-01-26T02:13:10.39Z");
    final Instant EXPIRED = Instant.parse("2024-01-26T03:32:32.39Z");

    @BeforeEach
    void beforeEach() {
        when(clock.instant()).thenReturn(CREATED);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        session = UserSession.create(SESSION_ID, USERNAME, clock);
    }

    @Test
    @DisplayName("[성공] 세션 생성")
    void create() {
        assertThat(session.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(session.getCreatedAt())
                .isEqualTo(LocalDateTime.ofInstant(CREATED, ZoneId.systemDefault()));
    }

    @Test
    @DisplayName("[성공] 세션 업데이트")
    void update() {
        when(clock.instant()).thenReturn(UPDATED);
        assertThat(session.update(clock).getLastAccessedAt())
                .isEqualTo(LocalDateTime.ofInstant(UPDATED, ZoneId.systemDefault()));
    }

    @Test
    @DisplayName("[성공] 유효 세션")
    void isValidate() {
        when(clock.instant()).thenReturn(UPDATED);
        assertThat(session.isValidate(clock)).isTrue();
    }

    @Test
    @DisplayName("[성공] 만기 세션")
    void isExpired() {
        when(clock.instant()).thenReturn(EXPIRED);
        assertThat(session.isValidate(clock)).isFalse();
    }
}