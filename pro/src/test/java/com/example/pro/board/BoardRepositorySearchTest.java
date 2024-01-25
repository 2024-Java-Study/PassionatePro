package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 이용
public class BoardRepositorySearchTest {

    @Autowired BoardRepository boardRepository;
    public static Board board;

    @BeforeEach
    public void setUp() {
        board = new Board("제목", "내용");
    }
    
    @Test
    @DisplayName("게시판 전체 조회")
    public void findAll() throws Exception {
        // given
        // static board
        Board board1 = new Board("제목", "내용");

        // when
        boardRepository.save(board);
        boardRepository.save(board1);
        List<Board> boardList = boardRepository.findAll();

        // then
        assertThat(boardList).isNotNull();
        assertThat(boardList.size()).isEqualTo(2);
    }
}
