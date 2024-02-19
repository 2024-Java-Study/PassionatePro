package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    public BoardSaveDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
