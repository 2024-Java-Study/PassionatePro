package com.example.pro.comment.controller;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.dto.CommentSaveRequestDto;
import com.example.pro.comment.dto.CommentUpdateRequestDto;
import com.example.pro.comment.service.CommentService;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final AuthService authService;
    private final CommentService commentService;

    @PostMapping
    public BasicResponse<String> saveComment(@Valid @RequestBody CommentSaveRequestDto saveRequest) {
        Member member = authService.loadUser();
        Comment comment = commentService.saveComment(member, saveRequest);
        return ResponseUtil.success("댓글이 성공적으로 등록되었습니다. Comment Id: " + comment.getId());
    }

    @PutMapping("/{commentId}")
    public BasicResponse<String> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto updateRequest) {
        Member member = authService.loadUser();
        Comment comment = commentService.updateComment(member, commentId, updateRequest);
        return ResponseUtil.success("댓글이 성공적으로 수정되었습니다. Comment Id: " + comment.getId());
    }

    @DeleteMapping("/{commentId}")
    public BasicResponse<String> deleteComment(@PathVariable Long commentId) {
        Member member = authService.loadUser();
        commentService.deleteComment(member, commentId);
        return ResponseUtil.success("댓글이 성공적으로 삭제되었습니다.");
    }
}
