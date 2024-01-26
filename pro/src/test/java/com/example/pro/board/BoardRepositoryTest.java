package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 이용
class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;

    public static Board board;

    @BeforeEach
    public void setUp() {
        board = new Board("제목", "내용");
    }

    @Test
    @DisplayName("게시글 생성")
    public void CreateBoard() throws Exception {
        // given
        // static board

        // when
        Board createdBoard = boardRepository.save(board);

        // then
        assertThat(board).isEqualTo(createdBoard);
        assertThat(board.getId()).isEqualTo(createdBoard.getId());
        assertThat(board.getTitle()).isEqualTo(createdBoard.getTitle());
        assertThat(board.getContent()).isEqualTo(createdBoard.getContent());
        assertThat(board.getCreated_at()).isEqualTo(createdBoard.getCreated_at());

    }
    
    @Test
    @DisplayName("Board 생성 시 null 값이 없어야 한다")
    public void validateCreateBoard() throws Exception {
        // given
        // static board

        // when
        Board createdBoard = boardRepository.save(board);

        // then
//        assertThat(createdBoard.getMember()).isNotNull();
        assertThat(createdBoard.getId()).isNotNull();
        assertThat(createdBoard.getTitle()).isNotNull();
        assertThat(createdBoard.getContent()).isNotNull();
    }

    @Test
    @DisplayName("게시글 전체 조회")
    public void findAll() throws Exception {
        // given
        // static board
        Board board1 = new Board("제목", "내용");

        // when
        boardRepository.save(board);
        boardRepository.save(board1);
        List<Board> boardList = boardRepository.findAll();

        // then
        assertThat(boardList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 단건 조회")
    public void findById() throws Exception {
        // given
        // static board

        // when
        Board createdBoard = boardRepository.save(board);
        Optional<Board> findBoard = boardRepository.findById(createdBoard.getId());

        // then
        assertThat(findBoard).isEqualTo(Optional.ofNullable(board));
    }
}