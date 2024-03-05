package com.example.pro.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReplyErrorCode {

    REPLY_NOT_FOUND("해당 id의 답글을 찾을 수 없습니다."),
    REPLY_ID_NOT_MATCH("요청한 댓글 id와 일치하지 않습니다."),
    REPLY_UPDATE_NOT_PERMITTED("해당 답글을 수정할 권한이 없습니다.");;

    private final String message;
}
