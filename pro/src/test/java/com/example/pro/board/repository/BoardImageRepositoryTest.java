package com.example.pro.board.repository;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 이용
class BoardImageRepositoryTest {

    @Autowired
    BoardImageRepository boardImageRepository;

    public static Board board;
    public static Member member;
    public static BoardImage boardImage;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .member(member)
                .title("제목")
                .content("내용")
                .build();

        boardImage = BoardImage.builder()
                .board(board)
                .url("test")
                .build();
    }

    @Test
    @DisplayName("[성공] 게시물 조회")
    void findByBoardId() throws Exception{
        // given
        // static board

        //when
//        Board createdBoard = boardImageRepository.findByBoardId(board.getId());

        //then
//        assertThat(createdBoard).isEqualTo(board);
    }
}