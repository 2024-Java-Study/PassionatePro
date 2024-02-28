package com.example.pro.comment.controller;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.dto.ReplySaveRequestDto;
import com.example.pro.comment.service.ReplyService;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;
    private final AuthService authService;

    @PostMapping
    public BasicResponse<String> saveReply(@Valid @RequestBody ReplySaveRequestDto saveRequest) {
        Member member = authService.loadUser();
        Reply reply = replyService.saveReply(member, saveRequest);
        return ResponseUtil.success("답글이 성공적으로 등록되었습니다. Reply Id: " + reply.getId());
    }
}
