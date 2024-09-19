package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CommentResponseDto(Long commentId, String profile, String username, Boolean isWriterQuit, String content, String createdAt, Boolean isDeleted, List<ReplyResponseDto> replies) {

    public static List<CommentResponseDto> makeDtoCollection(List<CommentQueryObject> comments, Map<Long, List<ReplyQueryObject>> repliesMap) {
        List<CommentResponseDto> responses = new ArrayList<>();
        comments.forEach(queryObject ->
            responses.add(queryObject.comment().isDeleted() ?
                    toDeletedCommentDto(queryObject, repliesMap.get(queryObject.comment().getId())==null? new ArrayList<>(): repliesMap.get(queryObject.comment().getId()))
                    : toCommentDto(queryObject, repliesMap.get(queryObject.comment().getId())==null? new ArrayList<>(): repliesMap.get(queryObject.comment().getId())))
        );
        return responses;
    }

    private static CommentResponseDto toCommentDto(CommentQueryObject response, List<ReplyQueryObject> replies) {
        return new CommentResponseDto(
                response.comment().getId(),
                response.writerProfile(),
                response.comment().getWriterName(),
                response.comment().isWriterQuitYn(),
                response.comment().getContent(),
                response.comment().getCreatedAt(),
                response.comment().isDeleted(),
                ReplyResponseDto.makeRepliesResponse(replies)
        );
    }

    private static CommentResponseDto toDeletedCommentDto(CommentQueryObject response, List<ReplyQueryObject> replies) {
        return new CommentResponseDto(
                response.comment().getId(),
                null,
                "(삭제)",
                true,
                "삭제된 댓글입니다.",
                response.comment().getCreatedAt(),
                response.comment().isDeleted(),
                ReplyResponseDto.makeRepliesResponse(replies)
        );
    }
}
