package com.example.pro.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardErrorCode {

    BOARD_NOT_FOUND("게시물을 찾을 수 없습니다."),
    UNAUTHORIZED_BOARD("게시물 권한이 없습니다.");

    private final String message;
}
