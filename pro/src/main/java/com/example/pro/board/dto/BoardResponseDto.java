package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {
    @NotBlank
    public String title;
    @NotBlank
    public String content;
    public String username;
    public String createdAt;
    public List<String> url;

    @Builder
    public BoardResponseDto(String username, String title, String content, String createdAt, List<String> url) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.url = url;
        this.createdAt = createdAt;
    }

    public static BoardResponseDto toBoardDto(Board board) {
        return BoardResponseDto.builder()
                .username(board.getMember().getUsername())
                .title(board.getTitle())
                .content(board.getContent())
                .url(BoardImageResponseDto.toBoardImageUrl(board.getImage()))
                .createdAt(String.valueOf(board.getCreatedAt()))
                .build();
    }
}
