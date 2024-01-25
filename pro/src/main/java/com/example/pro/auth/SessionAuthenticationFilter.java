package com.example.pro.auth;

import com.example.pro.auth.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.util.UUID;

import static com.example.pro.auth.domain.UserSession.SESSION_KEY;

@Slf4j
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {
    private final AuthProvider authProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String sessionId = CookieUtil.getCookieValue(request, SESSION_KEY);
        log.info("request from: '{}'", request.getServletPath());
        log.info("request session id: {}", sessionId);

        if (Strings.isBlank(sessionId)) {
            sessionId = UUID.randomUUID().toString();
        }
        CookieUtil.addCookie(response, SESSION_KEY, sessionId);
        Authentication authentication = authProvider.getAuthentication(sessionId);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
