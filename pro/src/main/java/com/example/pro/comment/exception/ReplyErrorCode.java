package com.example.pro.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReplyErrorCode {

    REPLY_ID_NOT_MATCH("요청한 댓글 id와 일치하지 않습니다.");

    private final String message;
}
