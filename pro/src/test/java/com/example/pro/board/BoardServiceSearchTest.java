package com.example.pro.board;

import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardListResponseDto;
import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.board.service.BoardService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceSearchTest {

    @Mock
    BoardRepository boardRepository; // 의존성 주입
    @InjectMocks
    BoardService boardService;
    public static Board board;
    public BoardSaveDto boardSaveDto;

    @BeforeEach
    public void setUp() {
        boardSaveDto = new BoardSaveDto("제목", "내용");
        board = BoardSaveDto.toBoardEntity(boardSaveDto);
    }

    @Test
    @DisplayName("게시글 생성")
    public void createBoard() throws Exception {
        // given
        // static board

        // when
        when(boardRepository.save(ArgumentMatchers.any())).thenReturn(board);

        // then
        assertThat(boardService.createBoard(boardSaveDto).getTitle()).isEqualTo("제목");
    }
    
    @Test
    @DisplayName("게시글 전체 조회")
    public void findAll() throws Exception {
        // given
        // static board
        List<Board> boardList = new ArrayList<>();
        boardList.add(board);

        // when
        when(boardRepository.findAll()).thenReturn(boardList);
        List<BoardListResponseDto> allBoards = boardService.findAllBoards(); // 모든 보드를 찾아 Dto로 바꿔주기

        // then
        assertThat(allBoards.size()).isEqualTo(1); // 전부 다 찾았는지 확인
    }
    
    @Test
    @DisplayName("게시글 단건 조회")
    public void findById() throws Exception {
        // given
        // static board
        Long boardId = 1L;
        
        // when
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        BoardResponseDto findBoard = boardService.findBoard(boardId);

        // then
        assertThat(findBoard.getTitle()).isEqualTo("제목");
    }

    
    @Test
    @DisplayName("게시글 단건 조회 시 게시글이 없으면 예외를 던진다")
    public void findByIdException() throws Exception {

        Long boardId = 1L;

        // boardRepository 동작 명시
        when(boardRepository.findById(boardId)).thenThrow(new IllegalArgumentException(
                "게시글이 존재하지 않습니다"));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            boardService.findBoard(boardId);
        });
    }

    @Test
    @DisplayName("제목으로 게시글을 찾아 리스트로 반환한다") // 두 글자 이상 같으면 List로 반환
    public void findByTitle() throws Exception {
        // given
        // static board
        List<Board> boardWithTitle = new ArrayList<>();
        boardWithTitle.add(board);
        String title = "제목";


        // when
        when(boardRepository.findByTitle(title)).thenReturn(boardWithTitle);
        List<BoardResponseDto> boardWithTitleList = boardService.searchTitle(title);

        // then
        assertThat(boardWithTitleList.size()).isEqualTo(1);
    }
}
