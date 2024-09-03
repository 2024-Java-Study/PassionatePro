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

        if (findBoardImages(board) != null) {
            boardImages.addAll(findBoardImages(board));
        }

        board.uploadFile(boardImages);
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

    private List<Long> findBoardImageIds (Board board) {
        return findBoardImages(board).stream()
                .map(BoardImage::getId)
                .toList();
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

    @Transactional
    public void updateBoardImage (List<MultipartFile> files, List<String> fileLists, Board board) {

        List<Long> boardImageIds = findBoardImageIds(board); // id 값으로 받아오기
        List<BoardImage> boardImages = findBoardImages(board);

        if (fileLists == null || fileLists.isEmpty()) {
            deleteFileAndImage(boardImages);
        } else if (boardImageIds.size() != fileLists.size()) {
            List<BoardImage> deleteBoardImages = findImageToDelete(fileLists, boardImageIds);
            deleteFileAndImage(deleteBoardImages);
        }

        if (files != null && !files.isEmpty()) {
            saveBoardImages(files, board);
        }
    }

    private void deleteFileAndImage(List<BoardImage> deleteBoardImages) {
        deleteS3File(deleteBoardImages);
        boardImageRepository.deleteAll(deleteBoardImages);
    }


    private List<BoardImage> findImageToDelete (List<String> fileLists, List<Long> boardImageIds) {
        List<Long> deleteBoardImageIds = new ArrayList<>();

        List<Long> fileImageList = fileLists.stream()
                .map(boardImageRepository::findByUrl)
                .map(BoardImage::getId)
                .toList();

        for (Long boardImageId : boardImageIds) {
            if (!fileImageList.contains(boardImageId)) {
                deleteBoardImageIds.add(boardImageId);
            }
        }

        return deleteBoardImageIds.stream()
                .map(id -> boardImageRepository.findById(id)
                        .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND)))
                .toList();
    }


    private void deleteS3File(List<BoardImage> deleteBoardImages) {
        deleteBoardImages.forEach(
                image -> fileUploader.deleteFile(image.getUrl(), BOARD_KEY)
        );
    }
}
