package com.example.pro.board.service;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.BoardImageResponseDto;
import com.example.pro.board.repository.BoardImageRepository;
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
    private final BoardImageRepository boardImageRepository;

    @Transactional
    public List<String> saveImages(List<MultipartFile> images) {
        List<String> urlList = new ArrayList<>();

        for (MultipartFile image : images) {
            String url = fileUploader.uploadFile(image, BOARD_KEY);
            urlList.add(url);
        }
        return urlList;
    }

    @Transactional
    public void uploadImages(Board board, List<String> urlList) {
        List<BoardImage> boardImages = new ArrayList<>();

        for (String url : urlList) {
            BoardImage boardImage = BoardImage.builder()
                    .board(board)
                    .url(url)
                    .build();
            boardImageRepository.save(boardImage);
            boardImages.add(boardImage);
        }
        board.uploadFile(boardImages);
    }

    @Transactional
    public BoardImageResponseDto imageListToDto(Board board) {
        List<BoardImageResponseDto> dtoList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();

        // List<BoardImage> -> List<String>
        for (BoardImage image : board.getImage()) {
            urlList.add(image.getUrl());
        }

        // dto 생성
        return BoardImageResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .username(board.getMember().getUsername())
                .createdAt(board.getCreatedAt())
                .urlList(urlList)
                .build();
    }
}
