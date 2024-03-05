package com.example.pro.board.service;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.BoardImageResponseDto;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
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

    public List<String> saveImages(List<MultipartFile> images) {
        List<String> urlList = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String url = fileUploader.uploadFile(image, BOARD_KEY);
                urlList.add(url);
            }
        }
        return urlList;
    }

    @Transactional
    public void uploadBoardImage(List<MultipartFile> file, Board board) {

        // 사진 업로드 -> 리스트 형태의 url
        List<String> urlList = saveImages(file);
        List<BoardImage> boardImages = new ArrayList<>();

        if (!(urlList == null) && !urlList.isEmpty()) {
            for (String url : urlList) {
                BoardImage boardImage = BoardImage.builder()
                        .board(board)
                        .url(url)
                        .build();
                boardImageRepository.save(boardImage);
                boardImages.add(boardImage);
            }
        }

        if (board == null) throw new BoardException(BoardErrorCode.BOARD_NOT_FOUND);
        else board.uploadFile(boardImages);
    }

    @Transactional
    public BoardImageResponseDto changeBoardImageToUrlList(Board board) {
        List<BoardImage> boardImages = findBoardImages(board);
        List<String> urlList = new ArrayList<>();

        // List<BoardImage> -> List<String>
        for (BoardImage image : boardImages) {
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

    public List<BoardImage> findBoardImages (Board board) {
        return board.getImage();
    }

    @Transactional
    public void deleteBoardImage (Board board) {
        List<String> urlList = changeBoardImageToUrlList(board).getUrlList();
        for (String url : urlList) {
            fileUploader.deleteFile(url);
        }

        List<BoardImage> boardImageList = boardImageRepository.findByBoardId(board.getId());
        boardImageRepository.deleteAll(boardImageList);
    }
}
