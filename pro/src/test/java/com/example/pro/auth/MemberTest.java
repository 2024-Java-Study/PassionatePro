package com.example.pro.auth;

import com.example.pro.auth.domain.Member;
import com.example.pro.common.exception.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("[성공] 객체 생성")
    void createMember() {
        Member member = Member.builder()
                .username("username")
                .password("password")
                .email("hello@gamil.com")
                .build();
        Assertions.assertThat(member.getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("[예외] 아이디 공백")
    void createMemberWithIdNull() {
        Assertions.assertThatThrownBy(() -> Member.builder()
                        .username("")
                        .password("password")
                        .email("hello@gmail.com").build()
                )
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("공백인 값이 포함되어 있습니다.");
    }

    @Test
    @DisplayName("[예외] 비밀번호 null")
    void createMemberWithPwNull() {
        Assertions.assertThatThrownBy(() ->  Member.builder()
                        .username("username")
                        .password(null)
                        .email("hello@gmail.com").build())
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("null값이 포함되어 있습니다.");
    }

    @Test
    @DisplayName("[예외] 올바르지 않은 이메일 형식")
    void createMemberWithInvalidEmail() {
        Assertions.assertThatThrownBy(() -> Member.builder()
                        .username("username")
                        .password("password")
                        .email("").build())
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("이메일 형식이 올바르지 않습니다.");
    }
}