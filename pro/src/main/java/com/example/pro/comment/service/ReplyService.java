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
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Reply saveReply(Member writer, ReplySaveRequestDto saveRequestDto) {
        Comment comment = commentRepository.findById(saveRequestDto.commentId())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        Reply reply = replyRepository.save(saveRequestDto.toReply(writer.getUsername(), comment));
        comment.getReplies().add(reply);
        return reply;
    }

    @Transactional
    public Reply updateReply(Member writer, Long replyId, ReplyUpdateRequestDto updateRequestDto) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND));
        checkPermission(writer, reply);
        reply.updateContent(updateRequestDto.content());
        return reply;
    }

    @Transactional
    public void deleteReplyFromDB(Member sessionUser, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND));
        checkPermission(sessionUser, reply);
        reply.deleteReply();
        deleteReplyFromDB(reply);
        deleteParentWithSiblings(reply.getComment());
    }

    private void deleteParentWithSiblings(Comment parent) {
        if (parent.countExistingReplies() == 0) {
            replyRepository.deleteAll(parent.getReplies());
            commentRepository.delete(parent);
        }
    }

    private void deleteReplyFromDB(Reply reply) {
        if (reply.isTheYoungest()) {
            replyRepository.delete(reply);
        }
    }

    private void checkPermission(Member writer, Reply reply) {
        if (! writer.getUsername().equals(reply.getWriter().getUsername())) {
            throw new ReplyException(ReplyErrorCode.REPLY_ACCESS_NOT_PERMITTED);
        }
    }
}
