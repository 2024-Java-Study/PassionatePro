package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
    private List<MultipartFile> images;

    @Builder
    public BoardSaveDto(String title, String content, List<MultipartFile> images) {
        this.title = title;
        this.content = content;
        this.images = images;
    }
}
