package com.example.pro.comment.domain;

import com.example.pro.board.domain.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {
    Board board;
    Comment comment;
    Reply reply1;
    Reply reply2;

    @BeforeEach
    void init() {
        board = Board.builder()
                .id(1L)
                .writerName("board-writer")
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        comment = Comment.builder()
                .id(1L)
                .username("comment-writer")
                .board(board)
                .content("댓글 내용 빈칸 아님")
                .build();

        board.getComments().add(comment);

        reply1 = Reply.builder()
                .id(1L)
                .writerName("board-writer")
                .comment(comment)
                .content("대댓글 내용1 빈칸 아님")
                .build();

        reply2 = Reply.builder()
                .id(2L)
                .writerName("comment-writer")
                .comment(comment)
                .content("대댓글 내용2 빈칸 아님")
                .build();
    }

    @Test
    @DisplayName("[성공] 댓글 내용 수정")
    void updateContent() {
        assertThat(comment.getContent()).isEqualTo("댓글 내용 빈칸 아님");
        comment.updateContent("댓글 내용 수정사항 반영");
        assertThat(comment.getContent()).isEqualTo("댓글 내용 수정사항 반영");
    }

    @Test
    @DisplayName("[성공] 댓글 삭제")
    void deleteComment() {
        assertThat(comment.isDeleted()).isFalse();
        comment.deleteComment();
        assertThat(comment.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("[성공] 댓글 하위 답글이 없으면 true")
    void hasNoReplies() {
        assertThat(comment.hasNoReplies()).isTrue();
    }

    @Test
    @DisplayName("[성공] 댓글 하위 답글이 있으면 false")
    void hasReplies() {
        comment.getReplies().add(reply1);
        assertThat(comment.hasNoReplies()).isFalse();
    }

    @Test
    @DisplayName("[성공] 댓글 하위 답글의 개수 count")
    void countExistingReplies() {
        comment.getReplies().add(reply1);
        comment.getReplies().add(reply2);
        assertThat(comment.countExistingReplies()).isEqualTo(2);
    }

    @Test
    @DisplayName("[성공] Soft Delete된 답글 포함 댓글 하위 답글의 개수 count")
    void countExistingRepliesAfterSoftDelete() {
        comment.getReplies().add(reply1);
        comment.getReplies().add(reply2);
        reply1.deleteReply();
        assertThat(comment.countExistingReplies()).isEqualTo(1);
    }
}