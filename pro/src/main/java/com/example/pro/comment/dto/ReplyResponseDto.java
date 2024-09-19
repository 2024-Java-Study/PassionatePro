package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Reply;

import java.util.ArrayList;
import java.util.List;

public record ReplyResponseDto(Long replyId, String username, String profile, boolean isWriterQuit, String content, String createdAt, boolean isDeleted) {

    public static List<ReplyResponseDto> makeRepliesResponse(List<ReplyQueryObject> replies) {
        List<ReplyResponseDto> response = new ArrayList<>();
        replies.forEach(queryObject ->
            response.add(queryObject.reply().isDeleted() ? toDeletedReplyResponseDto(queryObject) : toReplyResponseDto(queryObject))
        );
        return response;
    }

    private static ReplyResponseDto toReplyResponseDto(ReplyQueryObject queryObject) {
        return new ReplyResponseDto(
                queryObject.reply().getId(),
                queryObject.reply().getWriterName(),
                queryObject.writerProfile(),
                queryObject.reply().isWriterQuitYn(),
                queryObject.reply().getContent(),
                queryObject.reply().getCreatedAt(),
                queryObject.reply().isDeleted()
        );
    }

    private static ReplyResponseDto toDeletedReplyResponseDto(ReplyQueryObject queryObject) {
        return new ReplyResponseDto(
                queryObject.reply().getId(),
                "(삭제)",
                null,
                true,
                "삭제된 댓글입니다.",
                queryObject.reply().getCreatedAt(),
                queryObject.reply().isDeleted()
        );
    }
}
