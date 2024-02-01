package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

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
    public void updateBoard() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(board));

        String newTitle = "제목(new)";
        String newContent = "내용(new)";
        Board updateBoard = boardService.updateBoard(boardId, newTitle, newContent);


        // then
        assertThat(board.getTitle()).isEqualTo(updateBoard.getTitle());
        assertThat(board.getContent()).isEqualTo(updateBoard.getContent());
        assertThat(board).isEqualTo(updateBoard);
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deleteBoard() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(board));
        boardService.deleteBoard(1L);

        // then
        assertThrows(NullPointerException.class, () -> {
            boardService.findBoard(boardId);
        });
    }
}
