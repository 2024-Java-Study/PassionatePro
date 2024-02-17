package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.dto.BoardUpdateDto;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.repository.BoardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceTest {

    @Mock BoardRepository boardRepository;
    @Mock AuthService authService;
    @InjectMocks BoardService boardService;

    public BoardSaveDto boardSaveDto;
    public BoardUpdateDto boardUpdateDto;
    public static Board board;
    public static Member member;

    public static Long boardId = 1L, memberId = 1L;

    @BeforeEach
    public void setUp() {
        boardSaveDto = new BoardSaveDto("제목", "내용");

        member = new Member("ajeong", "password1234", "ajung7038@gmail.com");
        board = Board.builder()
                .member(member)
                .title(boardSaveDto.getTitle())
                .content(boardSaveDto.getContent())
                .build();

        boardUpdateDto = new BoardUpdateDto("제목(new)", "내용(new)");
        when(authService.loadUser().getId()).thenReturn(memberId);
    }


    @Test
    @DisplayName("[성공] 게시물 수정")
    public void updateBoard() throws Exception {
        // given
        // static board

        // when
//        when(authService.loadUser()).thenReturn(member);
//        when(authService.loadUser().getId()).thenReturn(memberId);
        when(board.getMember().getId()).thenReturn(memberId);

        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(board));


        BoardUpdateDto updateBoard = boardService.updateBoard(boardId, boardUpdateDto);

        // then
        assertThat(updateBoard.getTitle()).isEqualTo("제목(new)");
        assertThat(updateBoard.getContent()).isEqualTo("내용(new)");
    }

    @Test
    @DisplayName("[실패] 게시물 수정 - 게시물을 찾을 수 없는 경우")
    public void updateBoardWithNotFound() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        // then
        BoardException exception = assertThrows(BoardException.class, () -> {
            boardService.updateBoard(boardId, boardUpdateDto);
        });
        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }

    @Test
    @DisplayName("[실패] 게시물 삭제 - 게시물을 찾을 수 없는 경우")
    public void deleteBoardWithNotFound() throws Exception {
        // given
        // static board
        Long boardId = 1L;

        // when
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 삭제된 게시글에 접근 하려 할 때 예외 발생
        BoardException exception = assertThrows(BoardException.class, () -> {
            boardService.deleteBoard(boardId);
        });
        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }
}
