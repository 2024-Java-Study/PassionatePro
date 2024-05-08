package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Reply;

import java.util.ArrayList;
import java.util.List;

public record ReplyResponseDto(Long replyId, String username, String profile, boolean isWriterQuit, String content, String createdAt, boolean isDeleted) {

    public static List<ReplyResponseDto> makeRepliesResponse(List<Reply> replies) {
        List<ReplyResponseDto> response = new ArrayList<>();
        replies.forEach(son ->
            response.add(son.isDeleted() ? toDeletedReplyResponseDto(son) : toReplyResponseDto(son))
        );
        return response;
    }

    private static ReplyResponseDto toReplyResponseDto(Reply reply) {
        return new ReplyResponseDto(
                reply.getId(),
                reply.getWriter().getUsername(),
                reply.getWriter().getProfile(),
                reply.getWriter().isMemberQuit(),
                reply.getContent(),
                reply.getCreatedAt(),
                reply.isDeleted()
        );
    }

    private static ReplyResponseDto toDeletedReplyResponseDto(Reply reply) {
        return new ReplyResponseDto(
                reply.getId(),
                "(삭제)",
                null,
                true,
                "삭제된 댓글입니다.",
                reply.getCreatedAt(),
                reply.isDeleted()
        );
    }
}
