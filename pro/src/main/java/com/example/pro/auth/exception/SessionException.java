package com.example.pro.auth.exception;

import lombok.Getter;

@Getter
public class SessionException extends RuntimeException{

    private String code;
    private String message;

    public SessionException() {
        super();
        this.code = "EMPTY_SESSION";
        this.message = "세션 아이디 비어있음";
    }
}
