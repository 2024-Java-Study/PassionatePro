package com.example.pro.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(BoardSaveDto boardDto) {
        Board board = BoardSaveDto.toBoardEntity(boardDto);
        return boardRepository.save(board);
    }

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));

    }

    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    public List<Board> searchTitle(String title) {
        return boardRepository.findByTitle(title);
    }

    public Board updateBoard(Long boardId, String title, String content) {
        Optional<Board> board = boardRepository.findById(boardId);
        board = Optional.of(new Board(title, content));
        return null;
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new NullPointerException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));
        boardRepository.delete(board);
    }
}
