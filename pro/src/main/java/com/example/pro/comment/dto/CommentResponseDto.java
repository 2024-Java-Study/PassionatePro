package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Comment;

import java.util.ArrayList;
import java.util.List;

public record CommentResponseDto(Long commentId, String profile, String username, Boolean isWriterQuit, String content, String createdAt, Boolean isDeleted, List<ReplyResponseDto> replies) {

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
                comment.getWriter().getProfile(),
                comment.getWriter().getUsername(),
                comment.getWriter().isMemberQuit(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.isDeleted(),
                ReplyResponseDto.makeRepliesResponse(comment.getReplies())
        );
    }

    private static CommentResponseDto toDeletedCommentDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                null,
                "(삭제)",
                true,
                "삭제된 댓글입니다.",
                comment.getCreatedAt(),
                comment.isDeleted(),
                ReplyResponseDto.makeRepliesResponse(comment.getReplies())
        );
    }
}
