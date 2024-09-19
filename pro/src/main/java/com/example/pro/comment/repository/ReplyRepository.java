package com.example.pro.comment.repository;

import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.ReplyQueryObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select reply from Reply reply where reply.comment.id in :ids")
    List<ReplyQueryObject> findAllByCommentIds(@Param("ids") List<Long> commentIds);

    @Query("select reply from Reply reply where reply.writerName = :writerName")
    List<Reply> findAllByWriter(@Param("writerName") String writerName);
}
