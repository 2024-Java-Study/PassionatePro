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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.pro.auth.domain.UserSession.SESSION_KEY;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public BasicResponse<String> signUp(@Valid @RequestBody SignUpRequest form) {
        authService.signUp(form);
        return ResponseUtil.success("가입 성공");
    }

    @PostMapping("/login")
    public BasicResponse<String> signIn(@Valid @RequestBody LoginRequest form) {
        String username = authService.login(form);
        return ResponseUtil.success("로그인 성공: " + username);
    }

    @PostMapping("/logout")
    public BasicResponse<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        authService.logout();
        CookieUtil.deleteCookie(request, response, SESSION_KEY);
        return ResponseUtil.success("로그아웃되었습니다.");
    }

    @PutMapping("/profiles")
    public BasicResponse<String> updateProfile(@ModelAttribute ProfileUpdateRequest request) {
        Member member = authService.loadUser();
        member = memberService.updateProfile(request.getImage(), member);
        return ResponseUtil.success(member.getUsername() + "님의 프로필 이미지가 변경되었습니다.");
    }

    @DeleteMapping
    public BasicResponse<String> quitMember(HttpServletRequest request, HttpServletResponse response) {
        authService.quit();
        CookieUtil.deleteCookie(request, response, SESSION_KEY);
        return ResponseUtil.success("탈퇴되었습니다.");
    }

    @GetMapping("/me")
    public BasicResponse<String> requestMe() {
        String username = authService.loadUser().getUsername();
        return ResponseUtil.success("인증된 사용자: " + username);
    }
}
