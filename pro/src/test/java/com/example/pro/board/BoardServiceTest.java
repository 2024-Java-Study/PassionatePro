package com.example.pro.board;

import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.dto.BoardUpdateDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceTest {

    @Mock BoardRepository boardRepository;
    @InjectMocks BoardService boardService;

    public BoardResponseDto boardResponseDto;
    public BoardSaveDto boardSaveDto;
    public static Board board;

    @BeforeEach
    public void setUp() {
        boardSaveDto = new BoardSaveDto("제목", "내용");
        board = BoardSaveDto.toBoardEntity(boardSaveDto);
    }

    // board 단건 삭제, board 수정
    @Test
    @DisplayName("게시글 수정")
    public void updateBoard() throws Exception {
        // given
        // static board
        Long boardId = 1L;
        String newTitle = "제목(new)";
        String newContent = "내용(new)";

        // when
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(board));
        BoardUpdateDto updateBoard = boardService.updateBoard(boardId, newTitle, newContent);


        // then
        assertThat(updateBoard.getTitle()).isEqualTo("제목(new)");
        assertThat(updateBoard.getContent()).isEqualTo("내용(new)");
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deleteBoard() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(board)).thenReturn(null);
        boardService.deleteBoard(1L);

        // then
        // 삭제된 게시글에 접근 하려 할 때 예외 발생
        assertThrows(NullPointerException.class, () -> {
            boardService.findBoard(boardId);
        });
    }
}
