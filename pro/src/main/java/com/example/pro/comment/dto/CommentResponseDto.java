package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public record CommentResponseDto(String username, String content, String createdAt, List<ReplyResponseDto> replies) {

    public static CommentResponseDto toCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getMember().getUsername(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getReplies().stream().map(ReplyResponseDto::toReplyResponse).toList()
        );
    }
}
