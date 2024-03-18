package com.example.pro.comment.controller;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.ReplySaveRequestDto;
import com.example.pro.comment.dto.ReplyUpdateRequestDto;
import com.example.pro.comment.service.ReplyService;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;
    private final AuthService authService;

    @PostMapping
    public BasicResponse<String> saveReply(@Valid @RequestBody ReplySaveRequestDto saveRequest) {
        Member member = authService.loadUser();
        Reply reply = replyService.saveReply(member.getUsername(), saveRequest);
        return ResponseUtil.success("답글이 성공적으로 등록되었습니다. Reply Id: " + reply.getId());
    }

    @PutMapping("/{replyId}")
    public BasicResponse<String> updateReply(@PathVariable Long replyId, @Valid @RequestBody ReplyUpdateRequestDto updateRequest) {
        Member member = authService.loadUser();
        Reply reply = replyService.updateReply(member.getUsername(), replyId, updateRequest);
        return ResponseUtil.success("답글이 성공적으로 수정되었습니다. Reply Id: " + reply.getId());
    }

    @DeleteMapping("/{replyId}")
    public BasicResponse<String> deleteReply(@PathVariable Long replyId) {
        Member member = authService.loadUser();
        replyService.deleteReplyFromDB(member.getUsername(), replyId);
        return ResponseUtil.success("답글이 성공적으로 삭제되었습니다.");
    }
}
