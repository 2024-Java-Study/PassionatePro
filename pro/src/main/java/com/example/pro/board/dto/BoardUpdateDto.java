package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdateDto {

    @NotBlank
    public String title;
    @NotBlank
    public String content;

    private List<MultipartFile> images;
    private List<String> imageUrls;


    @Builder
    public BoardUpdateDto(String title, String content, List<MultipartFile> images, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.imageUrls = imageUrls;
    }


    public static BoardUpdateDto toBoardUpdateDto(Board board) {
        return BoardUpdateDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
