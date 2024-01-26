package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceSearchTest {

    @Mock BoardRepository boardRepository; // 의존성 주입
    @InjectMocks BoardService boardService;
//    public static Board board;

//    @BeforeEach
//    public void setUp() {
//        board = new Board("제목", "내용");
//        Long id = 1L;
//
//        // boardRepository 동작 명시
//        when(boardRepository.findById(id)).thenReturn(null);
//    }

    @Test
    @DisplayName("게시글 생성")
    public void createBoard() throws Exception {
        // given
        Board board = new Board("제목", "내용");

        // when
        when(boardRepository.save(board)).thenReturn(board);

        // then
        assertThat(boardService.createBoard(board)).isEqualTo(board);
    }
    
    @Test
    @DisplayName("게시글 전체 조회")
    public void findAll() throws Exception {
        // given
        Board board = new Board("제목", "내용");
        List<Board> boardList = new ArrayList<>();
        boardList.add(board);

        // when
        when(boardRepository.findAll()).thenReturn(boardList);
        List<Board> allBoards = boardService.findAllBoards();

        // then
        assertThat(allBoards).isEqualTo((boardList));
    }
    
    @Test
    @DisplayName("게시글 단건 조회")
    public void findById() throws Exception {
        // given
        Board board = new Board("제목", "내용");
        Long boardId = 1L;
        
        // when
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        Board findBoard = boardService.findBoard(1L);

        // then
        assertThat(findBoard).isEqualTo(board);
    }

    
    @Test
    @DisplayName("게시글 단건 조회 시 게시글이 없으면 예외를 던진다")
    public void findByIdException() throws Exception {

        Long boardId = 1L;
        // boardRepository 동작 명시
        when(boardRepository.findById(boardId)).thenThrow(new IllegalArgumentException((String.format(
                "게시글(%d)이 존재하지 않습니다", boardId))));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.findBoard(boardId);
        });
    }
}
