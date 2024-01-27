package com.example.pro.auth.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.dto.SignUpRequest;
import com.example.pro.auth.service.AuthService;
import com.example.pro.docs.ControllerTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.example.pro.auth.domain.UserSession.SESSION_KEY;
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

        perform.andDo(document("sign-up",
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

    // 가입 실패 -> 유효성 검사, 중복 회원

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