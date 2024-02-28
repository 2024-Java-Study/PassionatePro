package com.example.pro.comment.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException{

    private final CommentErrorCode code;
    private final String message;

    public CommentException(CommentErrorCode code) {
        super();
        this.code = code;
        this.message = code.getMessage();
    }

    public CommentException(CommentErrorCode code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
