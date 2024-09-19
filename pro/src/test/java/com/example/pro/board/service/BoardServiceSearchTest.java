package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.*;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.CommentQueryObject;
import com.example.pro.comment.dto.ReplyQueryObject;
import com.example.pro.files.FileUploader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Junit5 & Mockito 연동
public class BoardServiceSearchTest {

    @Mock
    BoardRepository boardRepository; // 의존성 주입
    @Mock BoardImageService boardImageService;
    @Mock FileUploader fileUploader;
    @InjectMocks BoardService boardService;


    public Board board;
    public static Member member;
    public BoardSaveDto boardSaveDto;

    public static final String URL = "https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg";
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
    }

    @Test
    @DisplayName("[성공] 게시물 생성")
    public void createBoard() throws Exception {
        // given
        // static board

        // when
        when(boardRepository.save(any())).thenReturn(board);
        doNothing().when(boardImageService).saveBoardImages(any(), any());

        // then
        assertThat(boardService.createBoard(boardSaveDto, member.getUsername()).getTitle()).isEqualTo("제목");
        assertThat(boardService.createBoard(boardSaveDto, member.getUsername()).getWriterName()).isEqualTo("ajeong7038");
    }
    
    @Test
    @DisplayName("[성공] 게시물 전체 조회")
    public void findAll() throws Exception {
        // given
        // static board
        List<Board> boardList = new ArrayList<>();
        boardList.add(board);

        // when
        when(boardRepository.findAll()).thenReturn(boardList);
        when(boardRepository.count()).thenReturn((long) boardList.size());
        BoardCountResponseDto allBoards = boardService.findAllBoards(); // 모든 보드를 찾아 Dto로 바꿔주기

        // then
        assertThat(allBoards.getTotal()).isEqualTo(1); // 전부 다 찾았는지 확인
    }
    
    @Test
    @DisplayName("[성공] 게시물 단건 조회")
    public void findById() throws Exception {
        // given
        // static board

        List<String> urlList = new ArrayList<>();
        urlList.add("https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg");

        BoardImage boardImage = BoardImage.builder()
                .board(board)
                .url(urlList.get(0))
                .build();

        List<BoardImage> boardImages = new ArrayList<>();
        boardImages.add(boardImage);

        board = Board.builder()
                .writerName(member.getUsername())
                .title("제목")
                .content("내용")
                .image(boardImages)
                .build();

        Comment comment = Comment.builder()
                .username("ajeong7038")
                .id(1L)
                .board(board)
                .content("댓글1 빈칸 아님")
                .build();

        List<CommentQueryObject> comments = new ArrayList<>();
        comments.add( new CommentQueryObject(comment, null));

        Reply reply = Reply.builder()
                .id(1L)
                .writerName("ajeong7038")
                .content("답글 내용 빈칸 아님")
                .comment(comment)
                .build();

        Map<Long, List<ReplyQueryObject>> repliesMap = new HashMap<>();

        BoardQueryDto boardQueryDto = new BoardQueryDto(new BoardWithWriterDto(board, null), boardImages, comments, repliesMap);

        board.getComments().add(comment);
        comment.getReplies().add(reply);
        
        // when
        when(boardRepository.findBoardDtoByBoardId(any())).thenReturn(boardQueryDto);
        BoardResponseDto boardResponse = boardService.makeBoardResponse(1L);

        // then
        assertThat(boardResponse.getTitle()).isEqualTo("제목");
        assertThat(boardResponse.getUsername()).isEqualTo("ajeong7038");
        assertThat(boardResponse.getProfile()).isEqualTo(member.getProfile());
    }

    
    @Test
    @DisplayName("[실패] 게시물 단건 조회 - 게시물이 없으면 예외를 던진다")
    public void findByIdException() throws Exception {

        // boardRepository 동작 명시
        when(boardRepository.findBoardDtoByBoardId(any())).thenThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        // then
        BoardException exception = assertThrows(BoardException.class, () -> {
            boardService.makeBoardResponse(1L);
        });

        assertThat(BoardErrorCode.BOARD_NOT_FOUND).isEqualTo(exception.getCode());
    }

    @Test
    @DisplayName("[성공] 제목으로 게시물을 찾아 리스트로 반환한다") // 두 글자 이상 같으면 List로 반환
    public void findByTitle() throws Exception {
        // given
        // static board
        List<Board> boardWithTitle = new ArrayList<>();
        boardWithTitle.add(board);
        String title = "제목";


        // when
        when(boardRepository.findByTitle(any())).thenReturn(boardWithTitle);
        BoardCountResponseDto dto = boardService.searchTitle(title);

        // then
        assertThat(dto.getTotal()).isEqualTo(1);
    }
}
