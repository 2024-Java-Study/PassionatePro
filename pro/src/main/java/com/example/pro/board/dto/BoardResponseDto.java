package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.comment.dto.CommentResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponseDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String username;
    private String profile;
    private Boolean isWriterQuit;
    private String createdAt;
    private List<String> urlList;
    private List<CommentResponseDto> comments;

    public static BoardResponseDto toBoardResponse(BoardQueryDto boardQueryDto) {

        return new BoardResponseDto(boardQueryDto.board().board().getTitle(),
                boardQueryDto.board().board().getContent(),
                boardQueryDto.board().board().getWriterName(),
                boardQueryDto.board().userProfile(),
                boardQueryDto.board().board().isWriterQuitYn(),
                boardQueryDto.board().board().getCreatedAt(),
                boardQueryDto.images().stream().map(BoardImage::getUrl).toList(),
                CommentResponseDto.makeDtoCollection(boardQueryDto.comments(), boardQueryDto.repliesMap())
        );
    }
}
