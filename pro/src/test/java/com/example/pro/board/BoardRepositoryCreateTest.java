package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 이용
class BoardRepositoryCreateTest {

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
        assertThat(createdBoard.getTitle()).isNotNull();
        assertThat(createdBoard.getContent()).isNotNull();
    }
}