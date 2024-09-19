package com.example.pro.comment.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(String writerName, CommentSaveRequestDto saveRequest) {
        Board board = boardRepository.findById(saveRequest.boardId())
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        Comment comment = commentRepository.save(saveRequest.toComment(board, writerName));
        board.getComments().add(comment);
        return comment;
    }

    @Transactional
    public Comment updateComment(String sessionUsername, Long commentId, CommentUpdateRequestDto updateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        checkPermission(sessionUsername, comment);
        comment.updateContent(updateRequest.content());
        return comment;
    }

    @Transactional
    public void deleteComment(String sessionUsername, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        checkPermission(sessionUsername, comment);
        comment.deleteComment();
        deleteCommentFromDB(comment);
    }

    private void deleteCommentFromDB(Comment comment) {
        if (comment.hasNoReplies()) {
            Board board = comment.getBoard();
            board.removeComment(comment);
            commentRepository.delete(comment);
        }
    }

    private void checkPermission(String sessionUsername, Comment comment) {
        if (! comment.getWriterName().equals(sessionUsername))
            throw new CommentException(CommentErrorCode.COMMENT_ACCESS_NOT_PERMITTED);
    }

}
