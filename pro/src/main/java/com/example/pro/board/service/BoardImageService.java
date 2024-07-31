package com.example.pro.board.service;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
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

    @Transactional
    public void saveBoardImages(List<MultipartFile> files, Board board) {

        // 사진 업로드 -> 리스트 형태의 url
        List<String> urls = uploadImages(files);
        List<BoardImage> boardImages = new ArrayList<>();

        if (!urls.isEmpty()) {
            for (String url : urls) {
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

    private List<String> uploadImages(List<MultipartFile> images) {
        List<String> urlList = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String url = fileUploader.uploadFile(image, BOARD_KEY);
                urlList.add(url);
            }
        }
        return urlList;
    }

    public List<String> getImageUrls(Board board) {
        List<BoardImage> boardImages = findBoardImages(board);
        List<String> urls = new ArrayList<>();

        // List<BoardImage> -> List<String>
        boardImages.forEach(boardImage -> urls.add(boardImage.getUrl()));
        return urls;
    }

    private List<BoardImage> findBoardImages (Board board) {
        return board.getImage();
    }

    @Transactional
    public void deleteBoardImage (Board board) {
        List<String> urlList = getImageUrls(board);
        for (String url : urlList) {
            fileUploader.deleteFile(url, BOARD_KEY);
        }

        List<BoardImage> boardImageList = boardImageRepository.findByBoardId(board.getId());
        boardImageRepository.deleteAll(boardImageList);
    }
}
