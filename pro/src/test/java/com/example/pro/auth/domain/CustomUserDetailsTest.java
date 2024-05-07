package com.example.pro.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

class CustomUserDetailsTest {

    Member member;

    @BeforeEach
    void beforeEach() {
        member = Member.builder()
                .username("username")
                .password("password")
                .nickname("nickname")
                .email("hello@gamil.com")
                .build();
    }

    @Test
    @DisplayName("[성공] 인증 권한 목록 반환")
    void getAuthorities() {
        CustomUserDetails userDetails = CustomUserDetails.create(member);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Assertions.assertThat(authorities.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("[성공] 인증 회원 아이디 반환")
    void getPassword() {
        CustomUserDetails userDetails = CustomUserDetails.create(member);
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("[성공] 인증 회원 비밀번호 반환")
    void getUsername() {
        CustomUserDetails userDetails = CustomUserDetails.create(member);
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(member.getUsername());
    }
}