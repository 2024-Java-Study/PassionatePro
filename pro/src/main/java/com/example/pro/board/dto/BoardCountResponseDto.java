package com.example.pro.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardCountResponseDto {
    private final Long total;
    private final List<BoardListResponseDto> boards;

    public BoardCountResponseDto(List<BoardListResponseDto> boards, Long count) {
        this.total = count;
        this.boards = boards;
    }
}
