package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardListResponseDto {

    @NotNull
    public Long id;
    public String username;
    public Boolean isWriterQuit;
    @NotBlank
    public String title;
    public String createdAt; // 날짜
    public String content;

    @Builder
    private BoardListResponseDto(Long id, String username, boolean isWriterQuit, String title, String createdAt, String content) {
        this.id = id;
        this.username = username;
        this.isWriterQuit = isWriterQuit;
        this.title = title;
        this.createdAt = createdAt;
        this.content = content;
    }

    public static BoardListResponseDto toBoardListDto(Board board) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .username(board.getWriterInfo().getUsername())
                .isWriterQuit(board.getWriterInfo().isMemberQuit())
                .title(board.getTitle())
                .createdAt(String.valueOf(board.getCreatedAt()))
                .content(board.getContent())
                .build();
    }
}
