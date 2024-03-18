package com.example.pro.comment.domain;

import com.example.pro.board.domain.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReplyTest {

    Board board;
    Comment comment;
    Reply reply1;
    Reply reply2;

    @BeforeEach
    void init() {
        board = Board.builder()
                .id(1L)
                .username("board-writer")
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        comment = Comment.builder()
                .id(2L)
                .username("comment-writer")
                .board(board)
                .content("댓글 내용 빈칸 아님")
                .build();

        board.getComments().add(comment);

        reply1 = Reply.builder()
                .id(1L)
                .username("board-writer")
                .comment(comment)
                .content("대댓글 내용1 빈칸 아님")
                .build();

        reply2 = Reply.builder()
                .id(1L)
                .username("comment-writer")
                .comment(comment)
                .content("대댓글 내용2 빈칸 아님")
                .build();

        comment.getReplies().add(reply1);
        comment.getReplies().add(reply2);
    }

    @Test
    @DisplayName("[성공] 답글 내용 수정")
    void updateContent() {
        assertThat(reply1.getContent()).isEqualTo("대댓글 내용1 빈칸 아님");
        reply1.updateContent("대댓글 내용1 수정 사항 반영");
        assertThat(reply1.getContent()).isEqualTo("대댓글 내용1 수정 사항 반영");
    }

    @Test
    @DisplayName("[성공] 답글 삭제-only soft delete")
    void deleteReply() {
        assertThat(reply1.isDeleted()).isFalse();
        reply1.deleteReply();
        assertThat(reply1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("[성공] 가장 마지막 댓글이면 true")
    void isTheYoungest() {
        assertThat(reply2.isTheYoungest()).isTrue();
    }

    @Test
    @DisplayName("[성공] 가장 마지막 댓글이 아니면 false")
    void isNotTheYoungest() {
        System.out.println(comment.getReplies().size());
        assertThat(reply1.isTheYoungest()).isFalse();
    }
}