package com.example.pro.board.service;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.BoardImageUploadDto;
import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.repository.BoardImageRepository;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.files.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;
//    private final BoardRepository boardRepository;
    private final FileUploader fileUploader;

    static final String BOARD_KEY = "boards/";

    public Board findBoard (Long boardId) {
//        return boardImageRepository.findByBoardId(boardId);
//                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        return boardImageRepository.findByBoardId(boardId).getBoard();
//                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND)));
    }

    @Transactional
    public void uploadBoardImage (BoardImageUploadDto dto, Board board) {
        List<MultipartFile> images = dto.getImages();
        List<BoardImage> imageList = new ArrayList<>();

        for (MultipartFile image : images) {
            String url = fileUploader.uploadFile(image, BOARD_KEY);
            BoardImage boardImage = BoardImage.builder()
                    .board(board)
                    .url(url)
                    .build();
            imageList.add(boardImage);
        }
    }
}
