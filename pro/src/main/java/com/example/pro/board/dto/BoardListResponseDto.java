package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
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
    public String title;
    public String createdAt; // 날짜

    @Builder
    public BoardListResponseDto(Long id, String username, String title, String createdAt) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.createdAt = createdAt;
    }

    public static BoardListResponseDto toBoardListDto(Board board) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .createdAt(String.valueOf(board.getCreatedAt()))
                .build();
    }
}
