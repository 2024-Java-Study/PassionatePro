package com.example.pro.board.dto;

import com.example.pro.board.domain.BoardImage;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @Builder
    public BoardImageResponseDto(String username, String title, String content, String createdAt, List<String> urlList) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.urlList = urlList;
        this.createdAt = createdAt;
    }
}
