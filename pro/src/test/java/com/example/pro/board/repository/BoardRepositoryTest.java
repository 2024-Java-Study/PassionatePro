package com.example.pro.board.repository;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@DataJpaTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 이용
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    public Board board;
    public Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .username(member.getUsername())
                .title("제목")
                .content("내용")
                .build();
    }

    @Test
    @DisplayName("게시물 생성")
    public void CreateBoard() throws Exception {
        // given
        // static board

        // when
        Board createdBoard = boardRepository.save(board);

        // then
        assertThat(createdBoard).isEqualTo(board);
        assertThat(createdBoard.getTitle()).isEqualTo("제목");
        assertThat(createdBoard.getContent()).isEqualTo("내용");

    }

    @Test
    @DisplayName("Board 생성 시 null 값이 없어야 한다")
    public void validateCreateBoard() throws Exception {
        // given
        // static board
        Board boardWithNull = Board.builder()
                .username(member.getUsername())
                .title(null)
                .content(null)
                .build();

        // when
        // then
        assertThrows(ConstraintViolationException.class, () -> {
            boardRepository.save(boardWithNull);
        });
    }

    @Test
    @DisplayName("게시물 전체 조회")
    public void findAll() throws Exception {
        // given
        // static board
        Board board1 = Board.builder()
                .username(member.getUsername())
                .title("제목")
                .content("내용")
                .build();

        // when
        boardRepository.save(board);
        boardRepository.save(board1);
        List<Board> boardList = boardRepository.findAll();

        // then
        assertThat(boardList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시물 단건 조회")
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