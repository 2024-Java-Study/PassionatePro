package com.example.pro.comment.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.ReplySaveRequestDto;
import com.example.pro.comment.exception.CommentErrorCode;
import com.example.pro.comment.exception.CommentException;
import com.example.pro.comment.repository.CommentRepository;
import com.example.pro.comment.repository.ReplyRepository;
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
class ReplyServiceTest {

    @Mock
    ReplyRepository replyRepository;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    ReplyService replyService;

    Member member;
    Board board;
    Comment comment;
    ReplySaveRequestDto saveRequest;

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
                .member(member)
                .board(board)
                .content("댓글 빈킨 아님")
                .build();

        saveRequest = new ReplySaveRequestDto(1L, "대댓글 내용");
    }

    @Test
    @DisplayName("[성공] 대댓글 작성")
    void saveReply() {
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        when(replyRepository.save(any())).thenReturn(saveRequest.toReply(member, comment));
        Reply reply = replyService.saveReply(member, saveRequest);

        assertThat(reply.getContent()).isEqualTo("대댓글 내용");
        assertThat(comment.getReplies().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("[실패] 대댓글 작성-잘못된 댓글 id")
    void commentNotFound() {
        when(commentRepository.findById(any()))
                .thenThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        assertThatThrownBy(() -> replyService.saveReply(member, saveRequest))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("해당 댓글을 찾을 수 없습니다.");
    }
}