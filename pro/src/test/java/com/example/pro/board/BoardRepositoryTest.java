package com.example.pro.board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // junit과 스프링을 엮어서 실행
@SpringBootTest // 스프링 컨테이너 안에서 테스트를 돌린다
@Transactional // 롤백
class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 생성")
    public void CreateBoard() throws Exception {
        // given
        Board board = new Board("제목", "내용");
        // when
        Board createBoard = boardRepository.save(board);

        // then
        Assertions.assertEquals(board, createBoard);
    }
}