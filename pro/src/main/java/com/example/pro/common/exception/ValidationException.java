package com.example.pro.common.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String message;

    public ValidationException() {
        super();
        this.message = "값이 유효하지 않습니다.";
    }

    public ValidationException(String message) {
        super();
        this.message = message;
    }
}
