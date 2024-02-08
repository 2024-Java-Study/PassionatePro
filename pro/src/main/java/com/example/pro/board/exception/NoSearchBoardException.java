package com.example.pro.board.exception;

import lombok.Getter;

@Getter
public class NoSearchBoardException extends RuntimeException {
    private BoardErrorCode code;
    private String message;

    public NoSearchBoardException(BoardErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public NoSearchBoardException(BoardErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
