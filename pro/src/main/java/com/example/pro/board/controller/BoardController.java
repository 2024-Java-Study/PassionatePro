package com.example.pro.board.controller;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.*;
import com.example.pro.board.service.BoardImageService;
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
    private final BoardImageService boardImageService;
    private final AuthService authService;

    @PostMapping
    public BasicResponse<String> create(@ModelAttribute @Valid BoardSaveDto boardDto) {
        Member member = authService.loadUser();
        Board board = boardService.createBoard(boardDto, member);
        boardImageService.uploadBoardImage(boardDto.getImages(), board);
        return ResponseUtil.success("게시물 생성에 성공하였습니다. 게시물id: " + board.getId());
    }

    @GetMapping("") // 전체 게시물 조회
    public BasicResponse<List<BoardListResponseDto>> findAll() {
        return ResponseUtil.success(boardService.findAllBoards());
    }

    @GetMapping("{id}") // 게시물 조회
    public BasicResponse<BoardImageResponseDto> findById(@PathVariable Long id) {
        Board board = boardService.findBoard(id);
    return ResponseUtil.success(boardImageService.changeBoardImageToUrlList(board));
    }

    @PutMapping("/{id}") // 게시물 수정
    public BasicResponse<BoardUpdateDto> update(@PathVariable Long id, @RequestBody @Valid BoardUpdateDto boardUpdateDto) {
        Member member = authService.loadUser();
        BoardUpdateDto board = boardService.updateBoard(id, boardUpdateDto, member);
        return ResponseUtil.success(board);
    }

    @DeleteMapping("/{id}") // 게시물 삭제
    public BasicResponse<String> delete(@PathVariable Long id) {
        Member member = authService.loadUser();
        boardService.deleteBoard(id, member);
        return ResponseUtil.success("게시물 삭제에 성공하였습니다. 게시물id: " + id);
    }

    @PutMapping("/{id}/upload")
    public BasicResponse<String> uploadImage(@ModelAttribute BoardImageUploadDto request, @PathVariable Long id) {
        Board board = boardService.findBoard(id);
        boardImageService.deleteBoardImage(board);
        boardImageService.uploadBoardImage(request.getImages(), board);
        return ResponseUtil.success("게시물 id: " + id + "번 사진 추가에 성공하였습니다.");
    }
}
