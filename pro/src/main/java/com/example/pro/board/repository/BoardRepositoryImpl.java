package com.example.pro.board.repository;

import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.BoardQueryDto;
import com.example.pro.board.dto.BoardWithWriterDto;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.comment.dto.CommentQueryObject;
import com.example.pro.comment.dto.ReplyQueryObject;
import com.example.pro.comment.repository.CommentRepository;
import com.example.pro.comment.repository.ReplyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardDataRepository {

    @PersistenceContext
    private final EntityManager em;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final BoardImageRepository boardImageRepository;

    @Override
    public BoardQueryDto findBoardDtoByBoardId(Long boardId) {
        BoardWithWriterDto board = findBoardWithWriter(boardId);
        List<BoardImage> boardImages = findBoardImages(boardId);
        List<CommentQueryObject> commentResults = findComments(boardId);
        Map<Long, List<ReplyQueryObject>> replyMap = findReplyMap(getIds(commentResults));
        return new BoardQueryDto(board, boardImages, commentResults, replyMap);
    }

    private List<Long> getIds(List<CommentQueryObject> commentResults) {
        return commentResults.stream().map(c -> c.comment().getId()).collect(Collectors.toList());
    }

    private List<BoardImage> findBoardImages(Long boardId) {
        return boardImageRepository.findAllByBoardId(boardId);
    }

    private List<CommentQueryObject> findComments(Long boardId) {
        return commentRepository.findAllByBoard(boardId);
    }

    private BoardWithWriterDto findBoardWithWriter(Long boardId) {
        // TODO: Member가 null일 때를 고려해야 함
        Object result = em.createQuery("select new com.example.pro.board.dto.BoardWithWriterDto(b, m.username) "
                + "from Board b left join Member m on b.writerName = m.username where b.id = :boardId").setParameter("boardId", boardId).getSingleResult();
        if (result instanceof BoardWithWriterDto) {
            return (BoardWithWriterDto) result;
        }
        throw new BoardException(BoardErrorCode.BOARD_NOT_FOUND);
    }

    private Map<Long, List<ReplyQueryObject>> findReplyMap(List<Long> commentIds) {
        List<ReplyQueryObject> replies = replyRepository.findAllByCommentIds(commentIds);
        return replies.stream().collect(Collectors.groupingBy(r -> r.reply().getComment().getId()));
    }
}
