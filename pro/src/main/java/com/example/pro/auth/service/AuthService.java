package com.example.pro.auth.service;

import com.example.pro.auth.repository.MemberRepository;
import com.example.pro.auth.repository.UserSessionRepository;
import com.example.pro.auth.dto.LoginRequest;
import com.example.pro.auth.domain.Member;
import com.example.pro.auth.dto.SignUpRequest;
import com.example.pro.auth.domain.UserSession;
import com.example.pro.auth.exception.AuthErrorCode;
import com.example.pro.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final UserSessionRepository sessionRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Transactional
    public long signUp(SignUpRequest request) {
        memberRepository.findByUsername(request.getUsername()).ifPresent(e -> {
            throw new AuthException(AuthErrorCode.MEMBER_DUPLICATED);
        });
        Member member = memberRepository.save(SignUpRequest.toMember(request, passwordEncoder));
        return member.getId();
    }

    @Transactional
    public String login(String sessionId, LoginRequest form) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword())
        );
        sessionRepository.save(UserSession.create(sessionId, form.getUsername(), clock));
        return form.getUsername();
    }

    public Member loadUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = memberRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new AuthException(AuthErrorCode.MEMBER_NOT_FOUND)
        );
        return user;
    }
}
