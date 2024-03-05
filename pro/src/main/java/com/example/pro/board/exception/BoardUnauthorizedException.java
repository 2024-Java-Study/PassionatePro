package com.example.pro.board.exception;


import lombok.Getter;

@Getter
public class BoardUnauthorizedException extends RuntimeException {
    private BoardErrorCode code;
    private String message;

    public BoardUnauthorizedException(BoardErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }
}
