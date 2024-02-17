package com.example.pro.auth.controller;


import com.example.pro.auth.domain.Member;
import com.example.pro.auth.dto.LoginRequest;
import com.example.pro.auth.dto.ProfileUpdateRequest;
import com.example.pro.auth.dto.SignUpRequest;
import com.example.pro.auth.service.AuthService;
import com.example.pro.auth.service.MemberService;
import com.example.pro.auth.utils.CookieUtil;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.pro.auth.domain.UserSession.SESSION_KEY;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public BasicResponse<String> signUp(@Valid @RequestBody SignUpRequest form) {
        authService.signUp(form);
        return ResponseUtil.success("가입 성공");
    }

    @PostMapping("/login")
    public BasicResponse<String> signIn(HttpServletRequest request, @Valid @RequestBody LoginRequest form) {
        String sessionId = CookieUtil.getCookieValue(request, SESSION_KEY);
        String username = authService.login(sessionId, form);
        return ResponseUtil.success("로그인 성공: " + username);
    }

    @PutMapping("/profiles")
    public BasicResponse<String> updateProfile(@ModelAttribute ProfileUpdateRequest request) {
        Member member = authService.loadUser();
        member = memberService.updateProfile(request.getImage(), member);
        return ResponseUtil.success(member.getUsername() + "님의 프로필 이미지가 변경되었습니다.");
    }

    @GetMapping("/me")
    public BasicResponse<String> requestMe() {
        String username = authService.loadUser().getUsername();
        return ResponseUtil.success("인증된 사용자: " + username);
    }
}
