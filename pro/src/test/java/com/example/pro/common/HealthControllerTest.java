package com.example.pro.common;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest extends ControllerTest {

    /**
     * Success Response 예시
     * {
     *   "success": true,
     *   "response": "API Health Check"
     * }
     * <p>
     * 문서화를 위한 controller test는 항상 추상클래스인 ControllerTest를 상속받아야 하고,
     * 오버라이딩한 initController()에서 Controller 객체 주입 후, 추상클래스의 mockMvc를 가져다 쓰면 됩니다.
     * 작성해보고 메서드가 인덱싱이 안되는 경우에는 static import를 참고하세요.
     * </p>
     */
    @Test
    @DisplayName("공통응답 테스트")
    void api_test() throws Exception {

        // when 요청을 보낼 때
        ResultActions perform = mockMvc.perform(get("/api/test")
                .contentType(MediaType.APPLICATION_JSON));

        // then 기대하는 응답
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("API Health Check"));

        // docs 문서화를 위한 부분
        perform.andDo(document("healthCheck",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Common")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())
        ));
    }

    @Override
    protected Object injectController() {
        return new HealthController();
    }
}