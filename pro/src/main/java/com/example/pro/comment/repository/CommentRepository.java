package com.example.pro.comment.repository;

import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.dto.CommentQueryObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select new com.example.pro.comment.dto.CommentQueryObject(c, m.profile) from Comment c join Member m on c.writerName = m.username where c.board.id = :boardId")
    List<CommentQueryObject> findAllByBoard(Long boardId);
    @Query("select comment from Comment comment where comment.writerName = :writerName")
    List<Comment> findAllByWriter(@Param("writerName") String writerName);
}
