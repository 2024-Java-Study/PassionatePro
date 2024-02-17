package com.example.pro.board.controller;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardListResponseDto;
import com.example.pro.board.dto.BoardResponseDto;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.dto.BoardUpdateDto;
import com.example.pro.board.service.BoardService;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final AuthService authService;

    @PostMapping
    public BasicResponse<String> create(@RequestBody @Valid BoardSaveDto boardDto) {
//        Member member = authService.loadUser();
        Board board = boardService.createBoard(boardDto);
        return ResponseUtil.success("게시물 생성에 성공하였습니다. 게시물id: " + board.getId());
    }

    @GetMapping("") // 전체 게시글 조회
    public BasicResponse<List<BoardListResponseDto>> findAll() {
        return ResponseUtil.success(boardService.findAllBoards());
    }

    @GetMapping("{id}") // 게시글 조회
    public BasicResponse<BoardResponseDto> findById(@PathVariable Long id) {
        BoardResponseDto board = boardService.findBoard(id);
        return ResponseUtil.success(board);
    }

    @PutMapping("/{id}") // 게시글 수정
    public BasicResponse<BoardUpdateDto> update(@PathVariable Long id, @RequestBody @Valid BoardUpdateDto boardUpdateDto) {
        BoardUpdateDto board = boardService.updateBoard(id, boardUpdateDto);
        return ResponseUtil.success(board);
    }

    @DeleteMapping("/{id}") // 게시글 삭제
    public BasicResponse<String> delete(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseUtil.success("게시물 삭제에 성공하였습니다. 게시물id: " + id);
    }
}
