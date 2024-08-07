package com.example.pro.board.repository;

import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.WriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitle(String title);

    @Query("select board from Board board where board.writerInfo.username = :writerName")
    List<Board> findAllByWriter(@Param("writerName") String writerName);
}
