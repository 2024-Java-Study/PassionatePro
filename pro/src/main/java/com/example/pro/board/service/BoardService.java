package com.example.pro.board.service;

import com.example.pro.board.repository.BoardRepository;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardListResponseDto;
import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.dto.BoardUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
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

//    public BoardUpdateDto updateBoard(Long boardId, String title, String content) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));
//
//        board.updateBoard(title, content);
//        return BoardUpdateDto.toBoardUpdateDto(board);
//    }

    @Transactional
    public BoardUpdateDto updateBoard(Long boardId, BoardUpdateDto boardUpdateDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));
        board.updateBoard(boardUpdateDto.getTitle(), boardUpdateDto.getContent());
        return BoardUpdateDto.toBoardUpdateDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new NullPointerException(String.format("게시글(%d)이 존재하지 않습니다", boardId)));
        boardRepository.delete(board);
    }
}
