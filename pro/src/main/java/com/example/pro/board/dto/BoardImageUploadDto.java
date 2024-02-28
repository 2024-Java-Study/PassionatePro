package com.example.pro.board.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImageUploadDto {
    List<MultipartFile> images;

    public BoardImageUploadDto(List<MultipartFile> images) {
        this.images = images;
    }
}
