package com.example.pro.board.service;

import com.example.pro.auth.service.AuthService;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
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
    private final AuthService authService;

    @Transactional
    public Board createBoard(BoardSaveDto boardDto) {
        Board board = Board.builder()
                .member(authService.loadUser())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();
        return boardRepository.save(board);
    }

    public BoardResponseDto findBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
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

    @Transactional
    public BoardUpdateDto updateBoard(Long boardId, BoardUpdateDto boardUpdateDto) {
        // 게시물 수정 로직
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        // 권한 확인 로직
        if (!authService.loadUser().getId().equals(board.getMember().getId())) {
            throw new BoardException(BoardErrorCode.UNAUTHORIZED_USER);
        }

        board.updateBoard(boardUpdateDto.getTitle(), boardUpdateDto.getContent());
        return BoardUpdateDto.toBoardUpdateDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        // 권한 확인 로직
        if (!authService.loadUser().getId().equals(board.getMember().getId())) {
            throw new BoardException(BoardErrorCode.UNAUTHORIZED_USER);
        }

        boardRepository.delete(board);
    }
}
