package com.example.pro.comment;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.comment.dto.CommentSaveRequestDto;
import com.example.pro.comment.service.CommentService;
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
@RequestMapping("/comments")
public class CommentController {

    private final AuthService authService;
    private final CommentService commentService;

    @PostMapping
    public BasicResponse<String> saveComment(@Valid @RequestBody CommentSaveRequestDto saveRequest) {
        Member member = authService.loadUser();
        Long commentId = commentService.saveComment(member, saveRequest);
        return ResponseUtil.success("댓글이 성공적으로 등록되었습니다. Comment Id: " + commentId);
    }
}
