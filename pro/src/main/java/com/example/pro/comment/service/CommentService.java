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
import com.example.pro.comment.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(Member writer, CommentSaveRequestDto saveRequest) {
        Board board = boardRepository.findById(saveRequest.boardId())
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        Comment comment = commentRepository.save(saveRequest.toComment(board, writer));
        board.getComments().add(comment);
        log.info("해당 게시글의 댓글 개수, {}", board.getComments().size());
        return comment;
    }

    @Transactional
    public Comment updateComment(Member sessionUser, Long commentId, CommentUpdateRequestDto updateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        if (! comment.getMember().equals(sessionUser))
            throw new CommentException(CommentErrorCode.COMMENT_UPDATE_NOT_PERMITTED);
        comment.updateContent(updateRequest.content());
        return comment;
    }
}
