package com.example.pro.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    public String username;
    public String content;
    public String createdAt;

    @Builder
    public CommentResponseDto (String username, String content) {
        this.username = username;
        this.content = content;
    }

//    public static CommentResponseDto toCommentDto() {
//        return null;
//    }
}
