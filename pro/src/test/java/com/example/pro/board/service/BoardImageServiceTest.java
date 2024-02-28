package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
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
    public static List<String> urlList;
    @BeforeEach
    public void setUp() {

        member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .member(member)
                .title("제목")
                .content("내용")
                .image(null)
                .build();

        urlList = new ArrayList<>();
        urlList.add(URL);
    }

    @Test
    @DisplayName("[성공] 게시물 저장")
    public void uploadFile() throws Exception {
        // 로직 : List<MultipartFile> images -> List<String> urlList
        // when
        // static board
        // urlList

        MultipartFile file = new MockMultipartFile("ForTest", new byte[]{});
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(file);

        when(fileUploader.uploadFile(any(), any()))
                .thenReturn(URL);

        // then
        assertThat(boardImageService.saveImages(multipartFiles).size()).isEqualTo(urlList.size());
    }

    @Test
    @DisplayName("[성공] 게시물 업로드")
    public void saveImage() throws Exception {
        // 로직 : List<String> urlList -> List<BoardImage> boardImageList
        // when
        List<BoardImage> boardImageList = new ArrayList<>();
        BoardImage boardImage = BoardImage.builder()
                .board(board)
                .url(URL)
                .build();
        boardImageList.add(boardImage);

        // when
        when(boardImageRepository.save(any())).thenReturn(boardImage);
        when(boardImageRepository.findByBoard(any())).thenReturn(boardImageList); //List<BoardImage>
        boardImageService.uploadImages(board, urlList);

        // then
        assertThat(board.getImage()).isEqualTo(boardImageList);
    }
}