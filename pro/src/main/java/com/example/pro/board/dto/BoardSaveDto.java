package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveDto {

    @NotNull
    private String title;
    private String content;

    @Builder
    public BoardSaveDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Board toBoardEntity(BoardSaveDto boardDto) {
        return Board.builder()
                .title(boardDto.title)
                .content(boardDto.content)
                .build();
    }
}
