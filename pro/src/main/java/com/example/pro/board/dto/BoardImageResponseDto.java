package com.example.pro.board.dto;

import com.example.pro.comment.dto.CommentResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImageResponseDto {
    @NotBlank
    public String title;
    @NotBlank
    public String content;
    @NotBlank
    public String username;
    public String createdAt;
    public List<String> urlList;
    public List<CommentResponseDto> comments;

    @Builder
    public BoardImageResponseDto(String username,
                                 String title,
                                 String content,
                                 String createdAt,
                                 List<String> urlList,
                                 List<CommentResponseDto> comments) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.urlList = urlList;
        this.createdAt = createdAt;
        this.comments = comments;
    }
}
