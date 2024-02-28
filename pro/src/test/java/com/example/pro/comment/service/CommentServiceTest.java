package com.example.pro.comment.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.dto.CommentSaveRequestDto;
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
    CommentSaveRequestDto request;
    Member member;
    Board board;

    @BeforeEach
    void init() {
        request = new CommentSaveRequestDto(1L, "댓글 내용");

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
    }

    @Test
    @DisplayName("[성공] 댓글 작성")
    void saveComment() {
        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(board));
        when(commentRepository.save(any())).thenReturn(request.toComment(board, member));

        Comment comment = commentService.saveComment(member, request);
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
    }
}