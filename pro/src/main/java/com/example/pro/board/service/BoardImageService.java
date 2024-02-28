package com.example.pro.board.service;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.files.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardImageService {

    private final FileUploader fileUploader;

    static final String BOARD_KEY = "boards/";

    @Transactional
    public void uploadBoardImage (List<MultipartFile> images, Board board) {
        List<BoardImage> imageList = new ArrayList<>();

        for (MultipartFile image : images) {
            String url = fileUploader.uploadFile(image, BOARD_KEY);
            BoardImage boardImage = BoardImage.builder()
                    .board(board)
                    .url(url)
                    .build();
            imageList.add(boardImage);
        }

        board.uploadFile(imageList);
    }
}
