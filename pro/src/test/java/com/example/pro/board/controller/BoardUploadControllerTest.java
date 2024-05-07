package com.example.pro.board.controller;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.BoardImageUploadDto;
import com.example.pro.board.service.BoardImageService;
import com.example.pro.board.service.BoardService;
import com.example.pro.docs.ControllerTest;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardUploadControllerTest extends ControllerTest {
    private final BoardService boardService = mock(BoardService.class);
    private final BoardImageService boardImageService = mock(BoardImageService.class);
    private final AuthService authService = mock(AuthService.class);

    static Board board;
//    static Board boardWithImage;
    private static final String SAMPLE_URL = "https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/e0aa3d71-b098-4926-a0da-2b14d64546fe.png";
//
//    static List<BoardImage> images = new ArrayList<>();

    @BeforeEach
    void init() {
         Member member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .username(member.getUsername())
                .title("제목")
                .content("내용")
                .build();
//
//        BoardImage boardImage = BoardImage.builder()
//                .board(board)
//                .url(SAMPLE_URL)
//                .build();
//
//        images.add(boardImage);
//
//        boardWithImage = Board.builder()
//                .member(member)
//                .title(board.getTitle())
//                .content(board.getContent())
//                .image(images)
//                .build();
    }
//    @Test
//    @DisplayName("[성공] 게시물 이미지 업로드")
//    void uploadFile() throws Exception {
//        MockMultipartFile image = new MockMultipartFile("image", "imageFile.jpeg", MediaType.IMAGE_JPEG_VALUE, "<<jpeg data>>".getBytes());
//        List<String> urlList = new ArrayList<>();
//        urlList.add(SAMPLE_URL);
//
//        when(boardService.findBoard(anyLong())).thenReturn(board);
//        when(boardImageService.saveImages(any())).thenReturn(urlList);
////        when(boardImageService.uploadImages(any(), any())).thenReturn()
//        doReturn(board);
//        MockMultipartHttpServletRequestBuilder builder = multipart("/boards/images");
//        builder.with(request -> {
//            request.setMethod("PUT");
//            return request;
//        });
//
//        ResultActions perform = mockMvc.perform(builder.file(image));
//
//        perform.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.response").value(USERNAME + "님의 프로필 이미지가 변경되었습니다."));
//
//    }

    @Override
    protected Object injectController() {
        return new BoardController(boardService, boardImageService, authService);
    }
}
