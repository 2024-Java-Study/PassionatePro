package com.example.pro.auth.service;

import com.example.pro.auth.AuthProvider;
import com.example.pro.auth.domain.Member;
import com.example.pro.auth.domain.UserSession;
import com.example.pro.auth.repository.MemberRepository;
import com.example.pro.auth.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SessionAuthenticationProvider implements AuthProvider {
    private final UserSessionRepository sessionRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public Authentication getAuthentication(String sessionId) {
        Optional<UserSession> optionalSession = sessionRepository.findById(sessionId);
        String username = sessionId;
        if (optionalSession.isPresent())
            username = optionalSession.get().getUsername();

        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "");
        if (optionalSession.isPresent() && optionalMember.isPresent() && optionalSession.get().isValidate(clock)) {
            Member member = optionalMember.get();
            sessionRepository.saveAndFlush(optionalSession.get().update(clock));
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().getName()));
            authentication = new UsernamePasswordAuthenticationToken(username, member.getPassword(), authorities);
        }
        return authentication;
    }
}
