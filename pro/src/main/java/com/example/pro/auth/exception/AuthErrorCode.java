package com.example.pro.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    MEMBER_DUPLICATED("이미 존재하는 회원입니다."),
    UNAUTHORIZED_USER("인증된 사용자가 아닙니다.");

    private final String message;
}
