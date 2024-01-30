package com.example.pro.auth.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.dto.SignUpRequest;
import com.example.pro.auth.exception.AuthErrorCode;
import com.example.pro.auth.exception.AuthException;
import com.example.pro.auth.service.AuthService;
import com.example.pro.docs.ControllerTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Locale;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.example.pro.auth.domain.UserSession.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MemberControllerTest extends ControllerTest {

    private final AuthService authService = mock(AuthService.class);

    @Test
    @DisplayName("[성공] 회원가입")
    void signUp() throws Exception {
        SignUpRequest request = new SignUpRequest("username", "password1234", "hello@gmail.com");
        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .cookie(new Cookie(SESSION_KEY, UUID.randomUUID().toString()))
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("가입 성공"));

        perform.andDo(document("sign up-success",
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
    @DisplayName("[실패] 이미 가입된 회원")
    void memberNotFound() throws Exception {
        Mockito.when(authService.signUp(any()))
                .thenThrow(new AuthException(AuthErrorCode.MEMBER_DUPLICATED));
        SignUpRequest request = new SignUpRequest("username", "password1234", "hello@gmail.com");
        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .cookie(new Cookie(SESSION_KEY, UUID.randomUUID().toString()))
        );

        perform.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(AuthException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("MEMBER_DUPLICATED"))
                .andExpect(jsonPath("$.response.errorMessage").value("이미 존재하는 회원입니다."));

        perform.andDo(document("sign up-duplicated member",
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
    @DisplayName("[실패] 가입정보 유효성 검사 실패")
    void signUpValidation() throws Exception {
        SignUpRequest request = new SignUpRequest(" ", " ", "hello");
        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.KOREAN)
                .content(body)
                .cookie(new Cookie(SESSION_KEY, UUID.randomUUID().toString()))
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BAD_INPUT"))
                .andExpect(jsonPath("$.response.errorMessage").value("입력이 올바르지 않습니다."))
                .andExpect(jsonPath("$.response.errors.username").value("공백일 수 없습니다"))
                .andExpect(jsonPath("$.response.errors.password").value("공백일 수 없습니다"))
                .andExpect(jsonPath("$.response.errors.email").value("올바른 형식의 이메일 주소여야 합니다"));

        perform.andDo(document("sign up-validation failed",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Auth")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors.username").type(JsonFieldType.STRING).description("아이디 공백 검사"),
                                fieldWithPath("response.errors.password").type(JsonFieldType.STRING).description("비밀번호 공백 검사"),
                                fieldWithPath("response.errors.email").type(JsonFieldType.STRING).description("이메일 형식 검사")
                        ).build())
        ));
    }

    @Test
    void signIn() {
    }
    // 로그인 실패 -> 세션 만료, 비밀번호 불일치, 없는 아이디

    @Test
    void requestMe() {
    }
    // 인증 실패

    @Override
    protected Object injectController() {
        return new MemberController(authService);
    }
}