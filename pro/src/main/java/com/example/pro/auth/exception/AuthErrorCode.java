package com.example.pro.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.");

    private String message;
}
