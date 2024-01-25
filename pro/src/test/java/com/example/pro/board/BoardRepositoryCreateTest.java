package com.example.pro.board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) // junit과 스프링을 엮어서 실행
@SpringBootTest // 스프링 컨테이너 안에서 테스트를 돌린다
@Transactional // 롤백
class BoardRepositoryCreateTest {

    @Autowired BoardRepository boardRepository;
//    Board board;

    @BeforeEach
    public void setUp() {
//        board = new Board("제목", "내용");
    }

    @Test
    @DisplayName("게시글 생성")
    public void CreateBoard() throws Exception {
        // given
        Board board = new Board("제목", "내용");

        // when
        Board createdBoard = boardRepository.save(board);

        // then
        assertThat(board).isEqualTo(createdBoard);
    }
    
    @Test
    @DisplayName("Board 생성 시 null 값이 없어야 한다")
    public void validateCreateBoard() throws Exception {
        // given
        Board board = new Board("제목", "내용");

        // then
//        assertThat(board.getMember()).isNotNull();
        assertThat(board.getTitle()).isNotNull();
        assertThat(board.getContent()).isNotNull();
    }
}