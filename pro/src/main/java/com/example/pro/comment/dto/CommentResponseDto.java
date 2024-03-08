package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Comment;

import java.util.ArrayList;
import java.util.List;

public record CommentResponseDto(String username, String content, String createdAt, List<ReplyResponseDto> replies) {

    public static List<CommentResponseDto> makeDtoCollection(List<Comment> comments) {
        List<CommentResponseDto> responses = new ArrayList<>();
        comments.forEach(comment -> responses.add(toCommentDto(comment)));
        return responses;
    }

    private static CommentResponseDto toCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getMember().getUsername(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getReplies().stream().map(ReplyResponseDto::toReplyResponse).toList()
        );
    }
}
