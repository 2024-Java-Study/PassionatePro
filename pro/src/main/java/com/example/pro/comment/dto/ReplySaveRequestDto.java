package com.example.pro.comment.dto;

import com.example.pro.auth.domain.Member;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.exception.ReplyErrorCode;
import com.example.pro.comment.exception.ReplyException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record ReplySaveRequestDto(@NotNull Long commentId, @NotBlank String content) {

    public ReplySaveRequestDto(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }

    public Reply toReply(Member member, Comment comment) {
        if (!Objects.equals(commentId, comment.getId()))
            throw new ReplyException(ReplyErrorCode.REPLY_ID_NOT_MATCH);

        return new Reply(member, comment, content);
    }
}
