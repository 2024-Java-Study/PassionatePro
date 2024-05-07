package com.example.pro.comment.repository;

import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.domain.WriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByComment(Comment comment);
    List<Reply> findAllByWriter(WriterInfo writer);
}
