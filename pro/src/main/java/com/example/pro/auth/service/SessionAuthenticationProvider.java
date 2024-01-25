package com.example.pro.auth.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.domain.UserSession;
import com.example.pro.auth.repository.MemberRepository;
import com.example.pro.auth.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SessionProvider {
    private final UserSessionRepository sessionRepository;
    private final MemberRepository memberRepository;

    public Authentication checkSession(String sessionId) {
        Optional<UserSession> optionalSession = sessionRepository.findById(sessionId);
        String username = sessionId;
        if (optionalSession.isPresent())
            username = optionalSession.get().getUsername();

        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "");
        if (optionalSession.isPresent() && optionalMember.isPresent() && optionalSession.get().isValidate()) {
            Member member = optionalMember.get();
            optionalSession.get().update();
            sessionRepository.saveAndFlush(optionalSession.get());
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(member.getRole().getName()));
            authentication = new UsernamePasswordAuthenticationToken(username, member.getPassword(), roles);
        }
        return authentication;
    }
}
