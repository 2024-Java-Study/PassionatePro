package com.example.pro.comment.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.CommentSaveRequestDto;
import com.example.pro.comment.dto.CommentUpdateRequestDto;
import com.example.pro.comment.exception.CommentErrorCode;
import com.example.pro.comment.exception.CommentException;
import com.example.pro.comment.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;
    @Mock
    BoardRepository boardRepository;
    @InjectMocks
    CommentService commentService;
    CommentUpdateRequestDto updateRequest;
    CommentSaveRequestDto saveRequest;
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
                .username(member.getUsername())
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        comment = Comment.builder()
                .id(1L)
                .board(board)
                .username(member.getUsername())
                .content("댓글 내용 빈칸 아님")
                .build();
    }

    @Test
    @DisplayName("[성공] 댓글 작성")
    void saveComment() {
        saveRequest = new CommentSaveRequestDto(1L, "댓글 내용");

        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(board));
        when(commentRepository.save(any())).thenReturn(saveRequest.toComment(board, member.getUsername()));

        Comment comment = commentService.saveComment(member.getUsername(), saveRequest);
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
        assertThat(board.getComments().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("[실패] 댓글 작성-존재하지 않는 게시글 id")
    void boardNotFound() {
        saveRequest = new CommentSaveRequestDto(1L, "댓글 내용");

        when(boardRepository.findById(1L)).thenThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        assertThatThrownBy(() -> commentService.saveComment(member.getUsername(), saveRequest))
                .isInstanceOf(BoardException.class)
                .hasMessageContaining("게시물을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[성공] 댓글 수정")
    void updateComment() {
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용. 빈 값 아님");
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        Comment updated = commentService.updateComment(member.getUsername(), 1L, updateRequest);
        assertThat(updated.getContent()).isEqualTo("수정된 댓글 내용. 빈 값 아님");
    }

    @Test
    @DisplayName("[실패] 댓글 수정-존재하지 않는 댓글id")
    void updateCommentIdNotFound() {
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용. 빈 값 아님");
        when(commentRepository.findById(any()))
                .thenThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        assertThatThrownBy(() -> commentService.updateComment(member.getUsername(), 1L, updateRequest));
    }

    @Test
    @DisplayName("[실패] 댓글 수정-수정 권한 없음")
    void updateCommentNotAuthorized() {
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용. 빈 값 아님");
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        assertThatThrownBy(() -> commentService.updateComment("comment-reader", 1L, updateRequest))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("해당 댓글에 접근할 권한이 없습니다.");
    }

    @Test
    @DisplayName("[성공] 댓글 삭제-하위 답글 없을 경우 hard delete")
    void hardDeleteComment() {
        board.getComments().add(comment);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        assertThat(board.getComments().size()).isNotZero();
        commentService.deleteComment(member.getUsername(), 1L);
        assertThat(board.getComments().size()).isZero();
    }

    @Test
    @DisplayName("[성공] 댓글 삭제-하위 답글 있을 경우 soft delete")
    void softDeleteComment() {
        board.getComments().add(comment);
        Reply reply = Reply.builder()
                .id(1L)
                .username(member.getUsername())
                .comment(comment)
                .content("댓글에 대한 답글 빈칸 아님")
                .build();
        comment.getReplies().add(reply);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        commentService.deleteComment(member.getUsername(), 1L);
        assertThat(board.getComments().size()).isNotZero();
        // todo: soft delete이므로 내부 로직에서는 삭제된 댓글까지 보여짐
    }

    @Test
    @DisplayName("[실패] 댓글 삭제-삭제 권한 없음")
    void deleteCommentNotPermitted() {
        board.getComments().add(comment);
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        assertThatThrownBy(() -> commentService.deleteComment("comment-reader", 1L))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("해당 댓글에 접근할 권한이 없습니다.");
    }

    @Test
    @DisplayName("[실패] 댓글 삭제-존재하지 않는 댓글id")
    void deleteCommentNotFound() {
        board.getComments().add(comment);
        when(commentRepository.findById(any()))
                .thenThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        assertThatThrownBy(() -> commentService.deleteComment("comment-writer", 1L))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("해당 댓글을 찾을 수 없습니다.");
    }

}