package com.example.pro.board.repository;

import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.WriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitle(String title);
    List<Board> findAllByWriterInfo(WriterInfo writer);
}
