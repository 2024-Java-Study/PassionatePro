package com.example.pro.board.dto;

import com.example.pro.board.domain.BoardImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImageResponseDto {
    public List<BoardImage> boardImageList;

    @Builder
    public BoardImageResponseDto(List<BoardImage> boardImageList) {
        this.boardImageList = boardImageList;
    }

    public static List<String> toBoardImageUrl(List<BoardImage> boardImages) {
        List<String> boardUrl = new ArrayList<>();
        for (BoardImage image : boardImages) {
            String url = image.getUrl();
            boardUrl.add(url);
        }
        return boardUrl;
    }
}
