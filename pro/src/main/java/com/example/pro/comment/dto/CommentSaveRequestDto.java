package com.example.pro.comment.dto;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class CommentSaveRequestDto {

    @Getter
    @NotNull
    private Long boardId;
    @NotBlank
    private String comment;

    public Comment toComment(Board board, Member member) {
        return Comment.builder()
                .board(board)
                .content(this.comment)
                .build();
    }
}
