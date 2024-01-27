package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceTest {

    @Mock BoardRepository boardRepository;
    @InjectMocks BoardService boardService;
    public static Board board;

    @BeforeEach
    public void setUp() {
        board = new Board("제목", "내용");
    }

    // board 단건 삭제, board 수정
    @Test
    @DisplayName("게시글 수정")
    public void BoardUpdate() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        boardService.updateBoard(boardId);


        // then
    }
}
