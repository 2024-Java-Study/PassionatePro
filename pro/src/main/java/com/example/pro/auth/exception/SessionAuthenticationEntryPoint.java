package com.example.pro.auth.exception;

import com.example.pro.common.response.ErrorEntity;
import com.example.pro.common.response.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SessionAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Auth Exception({})={}", AuthErrorCode.UNAUTHORIZED_USER.toString(), AuthErrorCode.UNAUTHORIZED_USER.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        String result = mapper.writeValueAsString(
                ResponseUtil.error(
                        new ErrorEntity(AuthErrorCode.UNAUTHORIZED_USER.toString(), AuthErrorCode.UNAUTHORIZED_USER.getMessage())
                )
        );
        response.getWriter().write(result);
    }
}
