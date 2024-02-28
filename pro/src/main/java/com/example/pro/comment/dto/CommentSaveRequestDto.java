package com.example.pro.comment.dto;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.exception.CommentErrorCode;
import com.example.pro.comment.exception.CommentException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Objects;

public class CommentSaveRequestDto {

    @Getter
    @NotNull
    private final Long boardId;
    @NotBlank
    private final String content;

    public Comment toComment(Board board, Member writer) {
        if (!Objects.equals(boardId, board.getId()))
            throw new CommentException(CommentErrorCode.COMMENT_ID_NOT_MATCH);

        return Comment.builder()
                .board(board)
                .member(writer)
                .content(this.content)
                .build();
    }

    public CommentSaveRequestDto(Long boardId, String content) {
        this.boardId = boardId;
        this.content = content;
    }
}
