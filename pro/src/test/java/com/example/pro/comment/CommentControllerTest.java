package com.example.pro.comment;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.dto.CommentSaveRequestDto;
import com.example.pro.comment.service.CommentService;
import com.example.pro.docs.ControllerTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest extends ControllerTest {

    @Mock
    CommentService commentService;
    @Mock
    AuthService authService;
    CommentSaveRequestDto request;
    Comment comment;
    Board board;
    Member member;

    @BeforeEach
    void init() {
        request = new CommentSaveRequestDto(1L, "댓글 내용 빈칸 아님");

        board = Board.builder()
                .id(1L)
                .member(member)
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        member = Member.builder()
                .username("comment-writer")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();

        comment = Comment.builder()
                .id(1L)
                .board(board)
                .member(member)
                .content("댓글 내용 빈칸 아님")
                .build();
    }

    @Test
    @DisplayName("[성공] 댓글 저장 API")
    void saveComment() throws Exception {
        when(authService.loadUser()).thenReturn(member);
        when(commentService.saveComment(any(), any())).thenReturn(comment);

        String body = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .locale(Locale.KOREAN)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("댓글이 성공적으로 등록되었습니다. Comment Id: 1"));

        perform.andDo(document("comment save-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Comment")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())
        ));
    }

    @Override
    protected Object injectController() {
        return new CommentController(authService, commentService);
    }
}