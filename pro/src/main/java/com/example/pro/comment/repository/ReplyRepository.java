package com.example.pro.comment.repository;

import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.domain.WriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByComment(Comment comment);
    @Query("select reply from Reply reply where reply.writer.username = :writerName")
    List<Reply> findAllByWriter(@Param("writerName") String writerName);
}
