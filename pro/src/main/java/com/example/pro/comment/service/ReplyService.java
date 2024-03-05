package com.example.pro.comment.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.ReplySaveRequestDto;
import com.example.pro.comment.dto.ReplyUpdateRequestDto;
import com.example.pro.comment.exception.CommentErrorCode;
import com.example.pro.comment.exception.CommentException;
import com.example.pro.comment.exception.ReplyErrorCode;
import com.example.pro.comment.exception.ReplyException;
import com.example.pro.comment.repository.CommentRepository;
import com.example.pro.comment.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Reply saveReply(Member writer, ReplySaveRequestDto saveRequestDto) {
        Comment comment = commentRepository.findById(saveRequestDto.commentId())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        Reply reply = replyRepository.save(saveRequestDto.toReply(writer, comment));
        comment.getReplies().add(reply);
        return reply;
    }

    @Transactional
    public Reply updateReply(Member writer, Long replyId, ReplyUpdateRequestDto updateRequestDto) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND));
        if (! writer.equals(reply.getMember()))
            throw new ReplyException(ReplyErrorCode.REPLY_UPDATE_NOT_PERMITTED);
        reply.updateContent(updateRequestDto.content());
        return reply;
    }
}
