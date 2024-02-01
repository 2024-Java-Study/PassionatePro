package com.example.pro.board;

import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public BasicResponse<String> create(@RequestBody @Valid BoardSaveDto boardDto) {
        Board board = boardService.createBoard(boardDto);
        return ResponseUtil.success("게시물 생성에 성공하였습니다. 게시물id: " + board.getId());
    }

}
