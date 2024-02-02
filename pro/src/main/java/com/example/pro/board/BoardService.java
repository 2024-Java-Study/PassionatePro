package com.example.pro.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardSaveDto boardSaveDto;
    private final BoardListResponseDto boardListResponseDto;

    public Board createBoard(BoardSaveDto boardDto) {
        Board board = BoardSaveDto.toBoardEntity(boardDto);
        return boardRepository.save(board);
    }

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));
    }

    public List<BoardListResponseDto> findAllBoards() {
        List<Board> boards = boardRepository.findAll();

        // 엔티티를 Dto로 변환하는 로직
        return boards.stream()
//                .map(board -> boardListResponseDto.toBoardDto(board))
                .map(BoardListResponseDto::toBoardDto)
                .collect(Collectors.toList());
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
