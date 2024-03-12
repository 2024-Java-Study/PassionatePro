package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Comment;

import java.util.ArrayList;
import java.util.List;

public record CommentResponseDto(Long commentId, String username, String content, String createdAt, Boolean isDeleted, List<ReplyResponseDto> replies) {

    public static List<CommentResponseDto> makeDtoCollection(List<Comment> comments) {
        List<CommentResponseDto> responses = new ArrayList<>();
        comments.forEach(comment ->
            responses.add(comment.isDeleted() ? toDeletedCommentDto(comment) : toCommentDto(comment))
        );
        return responses;
    }

    private static CommentResponseDto toCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getMember().getUsername(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.isDeleted(),
                comment.getReplies().stream().map(ReplyResponseDto::toReplyResponse).toList()
        );
    }

    private static CommentResponseDto toDeletedCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                "(삭제)",
                "삭제된 댓글입니다.",
                comment.getCreatedAt(),
                comment.isDeleted(),
                comment.getReplies().stream().map(ReplyResponseDto::toReplyResponse).toList()
        );
    }
}
