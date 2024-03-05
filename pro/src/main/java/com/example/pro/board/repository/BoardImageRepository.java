package com.example.pro.board.repository;

import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findByBoardId(Long boardId);
}
