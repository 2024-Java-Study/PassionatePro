package com.example.pro.auth.controller;


import com.example.pro.auth.dto.LoginRequest;
import com.example.pro.auth.dto.SignUpRequest;
import com.example.pro.auth.service.AuthService;
import com.example.pro.auth.utils.CookieUtil;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.pro.auth.domain.UserSession.SESSION_KEY;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;

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

    @GetMapping("/me")
    public BasicResponse<String> requestMe() {
        String username = authService.loadUser().getUsername();
        return ResponseUtil.success("인증된 사용자: " + username);
    }
}
