package com.example.pro.comment.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.comment.domain.Comment;
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
        board = Board.builder()
                .id(1L)
                .member(member)
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        member = Member.builder()
                .username("comment-writer")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
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

        Comment comment = commentService.saveComment(member, saveRequest);
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
        assertThat(board.getComments().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("[실패] 댓글 작성-존재하지 않는 게시글 id")
    void boardNotFound() {
        saveRequest = new CommentSaveRequestDto(1L, "댓글 내용");

        when(boardRepository.findById(1L)).thenThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        assertThatThrownBy(() -> commentService.saveComment(member, saveRequest))
                .isInstanceOf(BoardException.class)
                .hasMessageContaining("게시물을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[성공] 댓글 수정")
    void updateComment() {
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용. 빈 값 아님");
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        Comment updated = commentService.updateComment(member, 1L, updateRequest);
        assertThat(updated.getContent()).isEqualTo("수정된 댓글 내용. 빈 값 아님");
    }

    @Test
    @DisplayName("[실패] 댓글 수정-존재하지 않는 댓글id")
    void updateCommentIdNotFound() {
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용. 빈 값 아님");
        when(commentRepository.findById(any()))
                .thenThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        assertThatThrownBy(() -> commentService.updateComment(member, 1L, updateRequest));
    }

    @Test
    @DisplayName("[실패] 댓글 수정-존재하지 않는 댓글id")
    void updateCommentNotAuthorized() {
        Member otherMember =  Member.builder()
                .username("comment-reader")
                .password("password4321")
                .nickname("nickname22")
                .email("hellojava@gmail.com")
                .build();
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용. 빈 값 아님");
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));

        assertThatThrownBy(() -> commentService.updateComment(otherMember, 1L, updateRequest))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("해당 댓글을 수정할 권한이 없습니다.");
    }
}