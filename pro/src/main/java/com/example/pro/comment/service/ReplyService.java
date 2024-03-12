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
        Reply reply = replyRepository.save(saveRequestDto.toReply(writer, comment));
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
    public void deleteReply(Member sessionUser, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND));
        checkPermission(sessionUser, reply);
        reply.deleteReply();
        if (reply.hasNoSibling()) {
            Comment comment = reply.getComment();
            // TODO: 현재 지울 답글이 마지막 답글일 경우 답글도 지우고 댓글도 지우고
            replyRepository.delete(reply);
        }
    }

    private void checkPermission(Member writer, Reply reply) {
        if (! writer.equals(reply.getMember()))
            throw new ReplyException(ReplyErrorCode.REPLY_ACCESS_NOT_PERMITTED);
    }
}
