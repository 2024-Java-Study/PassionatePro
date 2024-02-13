package com.example.pro.auth.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.dto.BoardRequest;
import com.example.pro.auth.dto.LoginRequest;
import com.example.pro.auth.exception.AuthErrorCode;
import com.example.pro.auth.exception.AuthException;
import com.example.pro.auth.service.AuthService;
import com.example.pro.docs.ControllerTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.example.pro.auth.domain.UserSession.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MemberSignInControllerTest extends ControllerTest {
    private final AuthService authService = mock(AuthService.class);
    private static final String SESSION_ID = UUID.randomUUID().toString();
    private static final String USERNAME = "username";

    @Test
    @DisplayName("[성공] 로그인")
    void signInSuccess() throws Exception {
        when(authService.login(anyString(), any())).thenReturn(USERNAME);
        LoginRequest request = new LoginRequest(USERNAME, "password1234");
        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .cookie(new Cookie(SESSION_KEY, SESSION_ID))
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("로그인 성공: " + USERNAME));

        perform.andDo(document("sign in-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Auth")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 로그인-존재하지 않는 회원")
    void memberNotFound() throws Exception {
        when(authService.login(anyString(), any()))
                .thenThrow(new AuthException(AuthErrorCode.MEMBER_NOT_FOUND));
        LoginRequest request = new LoginRequest(USERNAME, "password1234");
        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .cookie(new Cookie(SESSION_KEY, SESSION_ID))
        );

        perform.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AuthException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("MEMBER_NOT_FOUND"))
                .andExpect(jsonPath("$.response.errorMessage").value("회원을 찾을 수 없습니다."));

        perform.andDo(document("sign in-member not found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Auth")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 로그인-틀린 비밀번호")
    void badCredential() throws Exception {
        when(authService.login(anyString(), any()))
                .thenThrow(new AuthException(AuthErrorCode.UNAUTHORIZED_USER));
        LoginRequest request = new LoginRequest(USERNAME, "password1234");
        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .cookie(new Cookie(SESSION_KEY, SESSION_ID))
        );

        perform.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AuthException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("UNAUTHORIZED_USER"))
                .andExpect(jsonPath("$.response.errorMessage").value("인증된 사용자가 아닙니다."));

        perform.andDo(document("sign in-bad credentials",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Auth")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[성공] 로그인 사용자 조회")
    void requestMe() throws Exception {
        BoardRequest boardRequest = BoardRequest.builder()
                .username(USERNAME)
                .build();

        when(authService.loadUser()).thenReturn(boardRequest);
        String userName = boardRequest.getUsername();

        ResultActions perform = mockMvc.perform(get("/members/me")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(SESSION_KEY, SESSION_ID))
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("인증된 사용자: " + userName));

        perform.andDo(document("request me-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Auth")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 로그인 사용자 조회-세션 만료")
    void sessionExpired() throws Exception {
        when(authService.loadUser())
                .thenThrow(new AuthException(AuthErrorCode.UNAUTHORIZED_USER));

        ResultActions perform = mockMvc.perform(get("/members/me")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(SESSION_KEY, SESSION_ID))
        );
        perform.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AuthException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("UNAUTHORIZED_USER"))
                .andExpect(jsonPath("$.response.errorMessage").value("인증된 사용자가 아닙니다."));

        perform.andDo(document("sign in-bad credentials",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Auth")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Override
    protected Object injectController() {
        return new MemberController(authService);
    }
}
