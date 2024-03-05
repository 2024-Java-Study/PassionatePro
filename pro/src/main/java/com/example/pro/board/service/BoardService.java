package com.example.pro.board.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.exception.BoardUnauthorizedException;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardListResponseDto;
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
    public Board createBoard(BoardSaveDto boardDto, Member member) {
        Board board = Board.builder()
                .member(member)
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();
        return boardRepository.save(board);
    }


    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    public List<BoardListResponseDto> findAllBoards() {
        List<Board> boards = boardRepository.findAll();

        // 엔티티를 Dto로 변환하는 로직
        return boards.stream()
                .map(BoardListResponseDto::toBoardListDto)
                .collect(Collectors.toList());
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
        if (!member.getUsername().equals(board.getMember().getUsername())) {
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
        if (!member.getUsername().equals(board.getMember().getUsername())) {
            throw new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_BOARD);
        }

        boardRepository.delete(board);
    }
}
