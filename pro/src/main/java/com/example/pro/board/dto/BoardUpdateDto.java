package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdateDto {

    @NotBlank
    public String title;
    @NotBlank
    public String content;


    @Builder
    public BoardUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public static BoardUpdateDto toBoardUpdateDto(Board board) {
        return BoardUpdateDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
