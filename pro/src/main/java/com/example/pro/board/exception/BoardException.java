package com.example.pro.board.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {
    private BoardErrorCode code;
    private String message;

    public BoardException(BoardErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public BoardException(BoardErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
