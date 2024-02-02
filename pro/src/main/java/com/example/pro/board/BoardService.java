package com.example.pro.board;

import com.example.pro.board.dto.BoardListResponseDto;
import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.dto.BoardSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(BoardSaveDto boardDto) {
        Board board = BoardSaveDto.toBoardEntity(boardDto);
        return boardRepository.save(board);
    }

    public BoardResponseDto findBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));
        return BoardResponseDto.toBoardDto(findBoard);
    }

    public List<BoardListResponseDto> findAllBoards() {
        List<Board> boards = boardRepository.findAll();

        // 엔티티를 Dto로 변환하는 로직
        return boards.stream()
                .map(BoardListResponseDto::toBoardListDto)
                .collect(Collectors.toList());
    }

    public List<BoardResponseDto> searchTitle(String title) {
        List<Board> boards = boardRepository.findByTitle(title);

        // 엔티티를 Dto로 변환하는 로직
        return boards.stream()
                .map(BoardResponseDto::toBoardDto)
                .collect(Collectors.toList());
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
