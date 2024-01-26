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

    public Board createBoard(Board board) {
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
}
