package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
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

    public static BoardResponseDto toBoardResponse(Board board, List<String> urlList) {
        return new BoardResponseDto(board.getTitle(),
                board.getContent(),
                board.getWriterInfo().getUsername(),
                board.getWriterInfo().getProfile(),
                board.getWriterInfo().isMemberQuit(),
                board.getCreatedAt(),
                urlList,
                CommentResponseDto.makeDtoCollection(board.getComments())
        );
    }
}
