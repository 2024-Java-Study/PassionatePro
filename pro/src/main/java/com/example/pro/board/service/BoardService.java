package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.dto.*;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.exception.BoardUnauthorizedException;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.board.domain.Board;
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
    private final BoardImageService boardImageService;

    @Transactional
    public Board createBoard(BoardSaveDto boardDto, String username, String profile) {
        Board board = Board.builder()
                .username(username)
                .profile(profile)
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();
        return boardRepository.save(board);
    }

    public BoardResponseDto makeBoardResponse(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        List<String> urls = boardImageService.getImageUrls(board);
        return BoardResponseDto.toBoardResponse(board, urls);
    }

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    public BoardCountResponseDto findAllBoards() {
        List<Board> boards = boardRepository.findAll();

        // 엔티티를 Dto로 변환하는 로직
        List<BoardListResponseDto> responses = boards.stream()
                .map(BoardListResponseDto::toBoardListDto)
                .collect(Collectors.toList());
        long count = boardRepository.count();
        return new BoardCountResponseDto(responses, count);
    }

    public List<Board> searchTitle(String title) {
        return boardRepository.findByTitle(title);

    }

    @Transactional
    public BoardUpdateDto updateBoard(Long boardId, BoardUpdateDto boardUpdateDto, Member member) {
        // 게시물 수정 로직
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        // 권한 확인 로직
        if (!member.getUsername().equals(board.getWriterInfo().getUsername())) {
            throw new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_BOARD);
        }

        board.updateBoard(boardUpdateDto.getTitle(), boardUpdateDto.getContent());
        return BoardUpdateDto.toBoardUpdateDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(
                        () -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        // 권한 확인 로직
        if (!member.getUsername().equals(board.getWriterInfo().getUsername())) {
            throw new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_BOARD);
        }

        boardRepository.delete(board);
    }
}
