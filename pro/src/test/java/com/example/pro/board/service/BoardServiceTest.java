package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.dto.BoardUpdateDto;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.NoSearchBoardException;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.board.service.BoardService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @InjectMocks
    BoardService boardService;

    public BoardSaveDto boardSaveDto;
    public BoardUpdateDto boardUpdateDto;
    public static Board board;

    @BeforeEach
    public void setUp() {
        boardSaveDto = new BoardSaveDto("제목", "내용");

        Member member = new Member("ajeong", "password1234", "ajung7038@gmail.com");
        board = Board.builder()
                .member(member)
                .title(boardSaveDto.getTitle())
                .content(boardSaveDto.getContent())
                .build();

        boardUpdateDto = new BoardUpdateDto("제목(new)", "내용(new)");
    }


    @Test
    @DisplayName("[성공] 게시글 수정")
    public void updateBoard() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(board));
        BoardUpdateDto updateBoard = boardService.updateBoard(boardId, boardUpdateDto);


        // then
        assertThat(updateBoard.getTitle()).isEqualTo("제목(new)");
        assertThat(updateBoard.getContent()).isEqualTo("내용(new)");
    }

    @Test
    @DisplayName("[실패] 게시글 수정 - 게시물을 찾을 수 없는 경우")
    public void updateBoardWithNotFound() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        // then
        NoSearchBoardException exception = assertThrows(NoSearchBoardException.class, () -> {
            boardService.updateBoard(boardId, boardUpdateDto);
        });
        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }

    @Test
    @DisplayName("[실패] 게시글 삭제 - 게시물을 찾을 수 없는 경우")
    public void deleteBoardWithNotFound() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 삭제된 게시글에 접근 하려 할 때 예외 발생
        NoSearchBoardException exception = assertThrows(NoSearchBoardException.class, () -> {
            boardService.deleteBoard(boardId);
        });
        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }
}
