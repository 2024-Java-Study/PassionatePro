package com.example.pro.board.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.board.domain.Board;
import com.example.pro.board.dto.BoardSaveDto;
import com.example.pro.board.service.BoardService;
import com.example.pro.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest extends ControllerTest {

    private final BoardService boardService = mock(BoardService.class);

    @Test
    @DisplayName("[성공] 게시물 생성")
    void create() throws Exception{
        BoardSaveDto dto = BoardSaveDto.builder()
                .title("제목")
                .content("내용")
                .build();

        Board board = BoardSaveDto.toBoardEntity(dto);
        when(boardService.createBoard(any())).thenReturn(board);
        String body = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response", containsString("게시물 생성에 성공하였습니다. 게시물id:")));

        perform.andDo(document("board creation-success"
        , preprocessRequest(prettyPrint())
        , preprocessResponse(prettyPrint())
        , resource(ResourceSnippetParameters.builder()
                        .tag("API-Board") // 큰 태그
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())));
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Override
    protected Object injectController() {
        return new BoardController(boardService);
    }
}