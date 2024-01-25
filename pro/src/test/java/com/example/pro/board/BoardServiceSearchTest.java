package com.example.pro.board;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 이용
public class BoardServiceSearchTest {

    @Autowired BoardRepository boardRepository;
    public static Board board;

    @BeforeEach
    public void setUp() {
        board = new Board("제목", "내용");
    }
    

    
    @Test
    @DisplayName("게시글 단건 조회 시 게시글이 없으면 예외를 던진다")
    public void findByIdWithNull() throws Exception {
        // given
        // static board
//
//        assertThrows(NotEnoughStockException.class, () -> {
//            orderService.order(member.getId(), book.getId(), orderCnt);
//        }, "NotEnoughStockException is accrued");

        // when
        Board createdBoard = boardRepository.save(board);
        Optional<Board> findBoard = boardRepository.findById(createdBoard.getId());

        // then
        assertThrows(IllegalStateException.class, () -> {


        });
    }
}
