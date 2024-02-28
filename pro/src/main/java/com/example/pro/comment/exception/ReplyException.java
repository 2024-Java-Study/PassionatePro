package com.example.pro.comment.exception;

import lombok.Getter;

@Getter
public class ReplyException extends RuntimeException{

    private final ReplyErrorCode code;
    private final String message;

    public ReplyException(ReplyErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public ReplyException(ReplyErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
