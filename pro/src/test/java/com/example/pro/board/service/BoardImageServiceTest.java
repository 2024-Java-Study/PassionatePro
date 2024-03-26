package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.BoardImageUploadDto;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.repository.BoardImageRepository;
import com.example.pro.files.FileUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardImageServiceTest {

    @Mock private FileUploader fileUploader;
    @Mock private BoardImageRepository boardImageRepository;
    @InjectMocks BoardImageService boardImageService;

    public static Board board;
    public static Member member;

    public static final String URL = "https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg";

    static MultipartFile file = new MockMultipartFile("ForTest", new byte[]{});

    static BoardImageUploadDto dto;
    @BeforeEach
    public void setUp() {

        member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .username(member.getUsername())
                .title("제목")
                .content("내용")
                .image(null)
                .build();

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(file);

        dto = BoardImageUploadDto.builder()
                .images(multipartFiles)
                .build();
    }

    @Test
    @DisplayName("[성공] 사진 저장")
    public void saveImages() throws Exception {
        // when
        // static board
        // urlList

        when(fileUploader.uploadFile(any(), any()))
                .thenReturn(URL);

        List<String> urlList = new ArrayList<>();
        urlList.add(URL);

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(file);

        // then
        assertThat(boardImageService.saveImages(multipartFiles).size()).isEqualTo(urlList.size());
    }

    @Test
    @DisplayName("[성공] 사진 저장 - 사진이 존재하지 않은 경우")
    void saveFileWithImageNull() throws Exception {
        // 로직 : List<MultipartFile> images -> List<String> urlList
        // when
        // static board
        List<MultipartFile> multipartFiles = new ArrayList<>();

        // then
        assertThat(boardImageService.saveImages(multipartFiles).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("[실패] 사진 저장 - 게시물을 찾을 수 없는 경우")
    void uploadImagesWithBoardNull() throws Exception {
        board = null;

        BoardImage boardImage = BoardImage.builder()
                .board(board)
                .url(URL)
                .build();

        // when
        when(boardImageRepository.save(any())).thenReturn(boardImage);

        // then
        BoardException exception = assertThrows(BoardException.class, () -> {
            boardImageService.uploadBoardImage(dto.getImages(), board);
        });
        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }

    @Test
    @DisplayName("[성공] 사진 업로드")
    public void uploadFile() throws Exception {
        // given
        List<BoardImage> boardImageList = new ArrayList<>();
        BoardImage boardImage = BoardImage.builder()
                .board(board)
                .url(URL)
                .build();
        boardImageList.add(boardImage);

        // when
        when(boardImageRepository.save(any())).thenReturn(boardImage);
        boardImageService.uploadBoardImage(dto.getImages(), board);

        // then
        assertThat(board.getImage().size()).isEqualTo(boardImageList.size());
    }
}