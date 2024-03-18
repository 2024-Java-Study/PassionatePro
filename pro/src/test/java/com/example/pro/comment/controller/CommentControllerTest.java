package com.example.pro.comment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.dto.CommentSaveRequestDto;
import com.example.pro.comment.dto.CommentUpdateRequestDto;
import com.example.pro.comment.exception.CommentErrorCode;
import com.example.pro.comment.exception.CommentException;
import com.example.pro.comment.service.CommentService;
import com.example.pro.docs.ControllerTest;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
    CommentSaveRequestDto saveRequest;
    CommentUpdateRequestDto updateRequest;
    Comment comment;
    Board board;
    Member member;

    @BeforeEach
    void init() {
        saveRequest = new CommentSaveRequestDto(1L, "댓글 내용 빈칸 아님");
        updateRequest = new CommentUpdateRequestDto("수정된 댓글 내용.");

        member = Member.builder()
                .username("comment-writer")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();

        board = Board.builder()
                .id(1L)
                .username(member.getUsername())
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        comment = Comment.builder()
                .id(1L)
                .board(board)
                .username(member.getUsername())
                .content("댓글 내용 빈칸 아님")
                .build();
    }

    @Test
    @DisplayName("[성공] 댓글 저장 API")
    void saveComment() throws Exception {
        when(authService.loadUser()).thenReturn(member);
        when(commentService.saveComment(any(), any())).thenReturn(comment);

        String body = objectMapper.writeValueAsString(saveRequest);

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

    @Test
    @DisplayName("[실패] 댓글 저장-유효성 검사 실패")
    void saveCommentValidation() throws Exception {
        saveRequest = new CommentSaveRequestDto(null, "  ");
        String body = objectMapper.writeValueAsString(saveRequest);

        ResultActions perform = mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.KOREAN)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BAD_INPUT"))
                .andExpect(jsonPath("$.response.errorMessage").value("입력이 올바르지 않습니다."))
                .andExpect(jsonPath("$.response.errors.boardId").value("널이어서는 안됩니다"))
                .andExpect(jsonPath("$.response.errors.content").value("공백일 수 없습니다"));

        perform.andDo(document("comment save-validation failed",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Comment")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors.boardId").type(JsonFieldType.STRING).description("게시물 id null 검사"),
                                fieldWithPath("response.errors.content").type(JsonFieldType.STRING).description("댓글 내용 공백 검사")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 댓글 저장-존재하지 않는 board id")
    void boardIdNotFound() throws Exception {
        when(authService.loadUser()).thenReturn(member);
        when(commentService.saveComment(any(), any()))
                .thenThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        String body = objectMapper.writeValueAsString(saveRequest);

        ResultActions perform = mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BoardException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BOARD_NOT_FOUND"))
                .andExpect(jsonPath("$.response.errorMessage").value("게시물을 찾을 수 없습니다."));

        perform.andDo(document("comment save-board not found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Comment")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[성공] 댓글 수정")
    void updateComment() throws Exception {
        when(authService.loadUser()).thenReturn(member);
        when(commentService.updateComment(any(), any(), any())).thenReturn(comment);
        String body = objectMapper.writeValueAsString(updateRequest);

        ResultActions perform = mockMvc.perform(put("/comments/{commentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("댓글이 성공적으로 수정되었습니다. Comment Id: 1"));

        perform.andDo(document("comment update-success",
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

    @Test
    @DisplayName("[실패] 댓글 수정-존재하지 않는 댓글id")
    void updateCommentIdNotFound() throws Exception {
        when(authService.loadUser()).thenReturn(member);
        when(commentService.updateComment(any(), any(), any()))
                .thenThrow(new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        String body = objectMapper.writeValueAsString(updateRequest);

        ResultActions perform = mockMvc.perform(put("/comments/{commentId}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(CommentException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("COMMENT_NOT_FOUND"))
                .andExpect(jsonPath("$.response.errorMessage").value("해당 댓글을 찾을 수 없습니다."));

        perform.andDo(document("comment update-comment not found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Comment")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 댓글 수정-댓글 수정 권한 없음")
    void updateCommentNotPermitted() throws Exception {
        when(authService.loadUser()).thenReturn(member);
        when(commentService.updateComment(any(), any(), any()))
                .thenThrow(new CommentException(CommentErrorCode.COMMENT_ACCESS_NOT_PERMITTED));

        String body = objectMapper.writeValueAsString(updateRequest);

        ResultActions perform = mockMvc.perform(put("/comments/{commentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(CommentException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("COMMENT_ACCESS_NOT_PERMITTED"))
                .andExpect(jsonPath("$.response.errorMessage").value("해당 댓글에 접근할 권한이 없습니다."));

        perform.andDo(document("comment update-not permitted",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Comment")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 댓글 수정-유효성 검사 실패")
    void updateReplyValidationFail() throws Exception {
        updateRequest = new CommentUpdateRequestDto("  ");

        String body = objectMapper.writeValueAsString(updateRequest);

        ResultActions perform = mockMvc.perform(put("/comments/{commentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.KOREAN)
                .content(body)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BAD_INPUT"))
                .andExpect(jsonPath("$.response.errorMessage").value("입력이 올바르지 않습니다."))
                .andExpect(jsonPath("$.response.errors.content").value("공백일 수 없습니다"));

        perform.andDo(document("comment update-validation failed",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Comment")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors.content").type(JsonFieldType.STRING).description("댓글 내용 공백 검사")
                        ).build())
        ));
    }

    @Override
    protected Object injectController() {
        return new CommentController(authService, commentService);
    }
}