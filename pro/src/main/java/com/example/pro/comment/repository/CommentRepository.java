package com.example.pro.comment.repository;

import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoard(Board board);
    @Query("select comment from Comment comment where comment.writer.username = :writerName")
    List<Comment> findAllByWriter(@Param("writerName") String writerName);
}
