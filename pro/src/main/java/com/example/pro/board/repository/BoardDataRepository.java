package com.example.pro.board.repository;

import com.example.pro.board.dto.BoardQueryDto;

public interface BoardDataRepository {

    BoardQueryDto findBoardDtoByBoardId(Long boardId);
}
