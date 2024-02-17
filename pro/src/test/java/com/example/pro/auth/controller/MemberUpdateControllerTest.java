package com.example.pro.auth.controller;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.auth.service.MemberService;
import com.example.pro.docs.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.example.pro.auth.controller.MemberSignInControllerTest.USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MemberUpdateControllerTest extends ControllerTest {

    private final AuthService authService = mock(AuthService.class);
    private final MemberService memberService = mock(MemberService.class);
    private Member member;
    private static final String SAMPLE_URL = "https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/e0aa3d71-b098-4926-a0da-2b14d64546fe.png";

    @BeforeEach
    void init() {
        member = Member.builder()
                .username("username")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();
    }

    @Test
    @DisplayName("[성공] 프로필이미지 변경 요청")
    void updateProfile() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "imageFile.jpeg", MediaType.IMAGE_JPEG_VALUE, "<<jpeg data>>".getBytes());
        when(authService.loadUser()).thenReturn(member);
        when(memberService.updateProfile(any(), any())).thenReturn(member.updateProfile(SAMPLE_URL));

        MockMultipartHttpServletRequestBuilder builder = multipart("/members/profiles");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        ResultActions perform = mockMvc.perform(builder.file(image));

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(USERNAME + "님의 프로필 이미지가 변경되었습니다."));

        perform.andDo(document("profile update-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
               // requestPartFields("image", fieldWithPath("image").description("변경할 이미지 파일")),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Member")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())
        ));
    }

    @Override
    protected Object injectController() {
        return new MemberController(authService, memberService);
    }
}
