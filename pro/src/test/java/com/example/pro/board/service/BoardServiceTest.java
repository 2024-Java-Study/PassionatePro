package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @Mock BoardImageService boardImageService;
    @InjectMocks BoardService boardService;

    public BoardSaveDto boardSaveDto;
    public BoardUpdateDto boardUpdateDto;
    public static Board board;
    public static Member member;

    public static Long boardId = 1L, memberId = 1L;
    static MultipartFile file = new MockMultipartFile("ForTest", new byte[]{});

    @BeforeEach
    public void setUp() {
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);

        boardSaveDto = BoardSaveDto.builder()
                .title("제목")
                .content("내용")
                .images(fileList)
                .build();

        member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .writerName(member.getUsername())
                .title(boardSaveDto.getTitle())
                .content(boardSaveDto.getContent())
                .image(null)
                .build();

        boardUpdateDto = BoardUpdateDto.builder()
                .title("제목(new)")
                .content("내용(new)")
                .images(fileList)
                .build();
    }


    @Test
    @DisplayName("[성공] 게시물 수정")
    public void updateBoard() throws Exception {
        // given
        // static board

        // when
        when(boardRepository.findById(anyLong())).thenReturn(Optional.ofNullable(board));
        doNothing().when(boardImageService).updateBoardImage(any(), any(), any());

        Board updateBoard = boardService.updateBoard(boardId, boardUpdateDto, member);

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
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());
        // then
        BoardException exception = assertThrows(BoardException.class, () -> {
            boardService.updateBoard(boardId, boardUpdateDto, member);
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
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        // 삭제된 게시글에 접근 하려 할 때 예외 발생
        BoardException exception = assertThrows(BoardException.class, () -> {
            boardService.deleteBoard(boardId, member);
        });
        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }
}
