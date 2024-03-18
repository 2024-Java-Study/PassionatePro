package com.example.pro.comment.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.ReplySaveRequestDto;
import com.example.pro.comment.dto.ReplyUpdateRequestDto;
import com.example.pro.comment.exception.CommentErrorCode;
import com.example.pro.comment.exception.CommentException;
import com.example.pro.comment.exception.ReplyErrorCode;
import com.example.pro.comment.exception.ReplyException;
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
    Reply reply;
    ReplySaveRequestDto saveRequest;
    ReplyUpdateRequestDto updateRequest;

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
                .username(member.getUsername())
                .board(board)
                .content("댓글 빈킨 아님")
                .build();

        reply = Reply.builder()
                .id(1L)
                .username(member.getUsername())
                .comment(comment)
                .content("댓글에 대한 답글 빈칸 아님")
                .build();

        saveRequest = new ReplySaveRequestDto(1L, "대댓글 내용");
        updateRequest = new ReplyUpdateRequestDto("수정된 내용의 답글");
    }

    @Test
    @DisplayName("[성공] 대댓글 작성")
    void saveReply() {
        when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(comment));
        when(replyRepository.save(any())).thenReturn(saveRequest.toReply(member.getUsername(), comment));
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

    @Test
    @DisplayName("[성공] 대댓글 수정")
    void updateReply() {
        when(replyRepository.findById(any())).thenReturn(Optional.ofNullable(reply));
        Reply updated = replyService.updateReply(member, 1L, updateRequest);
        assertThat(updated.getContent()).isEqualTo("수정된 내용의 답글");
    }

    @Test
    @DisplayName("[실패] 대댓글 수정-존재하지 않는 답글id")
    void updateReplyIdNotFound() {
        when(replyRepository.findById(any()))
                .thenThrow(new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND));

        assertThatThrownBy(() -> replyService.updateReply(member, 2L, updateRequest))
                .isInstanceOf(ReplyException.class)
                .hasMessageContaining("해당 id의 답글을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[실패] 대댓글 수정-권한 없음")
    void updateReplyNotPermitted() {
        Member otherMember = Member.builder()
                .username("not-writer")
                .password("password4321")
                .nickname("nickname11")
                .email("helloworld@naver.com")
                .build();

        when(replyRepository.findById(any())).thenReturn(Optional.ofNullable(reply));
        assertThatThrownBy(() -> replyService.updateReply(otherMember, 1L, updateRequest))
                .isInstanceOf(ReplyException.class)
                .hasMessageContaining("해당 답글에 접근할 권한이 없습니다.");
    }
}