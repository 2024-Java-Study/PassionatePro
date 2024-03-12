package com.example.pro.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentErrorCode {

    COMMENT_NOT_FOUND("해당 댓글을 찾을 수 없습니다."),
    COMMENT_ID_NOT_MATCH("요청한 게시글 id와 일치하지 않습니다."),
    COMMENT_ACCESS_NOT_PERMITTED("해당 댓글에 접근할 권한이 없습니다.");

    private final String message;
}
