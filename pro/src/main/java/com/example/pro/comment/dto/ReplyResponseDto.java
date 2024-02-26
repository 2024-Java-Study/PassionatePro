package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Reply;
import lombok.Getter;

@Getter
public record ReplyResponseDto(String username, String content, String createdAt) {

    public static ReplyResponseDto toReplyResponse(Reply reply) {
        return new ReplyResponseDto(reply.getMember().getUsername(), reply.getContent(), reply.getCreatedAt());
    }
}
