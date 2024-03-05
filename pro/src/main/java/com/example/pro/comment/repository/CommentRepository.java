package com.example.pro.comment.repository;

import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoard(Board board);
}
