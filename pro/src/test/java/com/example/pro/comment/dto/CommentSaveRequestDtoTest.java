package com.example.pro.comment.dto;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.exception.CommentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentSaveRequestDtoTest {

    CommentSaveRequestDto request;
    Board board;
    Member member;

    @BeforeEach
    void init() {
        request = new CommentSaveRequestDto(1L, "댓글 내용");

        member = Member.builder()
                .username("comment-writer")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();

        board = Board.builder()
                .id(2L)
                .writerName(member.getUsername())
                .title("게시글 제목")
                .content("게시글 내용")
                .build();
    }

    @Test
    @DisplayName("[실패] board 식별자가 일치하지 않는 경우")
    void dtoToEntity() {
        assertThatThrownBy(() -> request.toComment(board, member.getUsername()))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("요청한 게시글 id와 일치하지 않습니다.");
    }
}