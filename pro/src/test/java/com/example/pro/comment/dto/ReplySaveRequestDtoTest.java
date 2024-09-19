package com.example.pro.comment.dto;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.exception.ReplyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplySaveRequestDtoTest {

    Member member;
    Board board;
    Comment comment;

    @BeforeEach
    void init() {
        member = Member.builder()
                .username("comment-writer")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();

        board = Board.builder()
                .id(1L)
                .writerName(member.getUsername())
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        comment = Comment.builder()
                .id(2L)
                .board(board)
                .content("댓글 내용 빈칸 아님")
                .build();
    }

    @Test
    @DisplayName("[실패] comment id 불일치")
    void toReply() {
        ReplySaveRequestDto saveRequest = new ReplySaveRequestDto(1L, "답글 또는 대댓글");
        assertThatThrownBy(() -> saveRequest.toReply(member.getUsername(), comment))
                .isInstanceOf(ReplyException.class)
                .hasMessageContaining("요청한 댓글 id와 일치하지 않습니다.");
    }
}