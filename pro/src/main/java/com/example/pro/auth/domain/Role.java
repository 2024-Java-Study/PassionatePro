package com.example.pro.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("admin", "관리자"), USER("user", "회원");


    private final String key;
    private final String name;
}
