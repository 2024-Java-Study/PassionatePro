package com.example.pro.board.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.pro.auth.domain.Member;
import com.example.pro.auth.service.AuthService;
import com.example.pro.board.domain.Board;
import com.example.pro.board.domain.BoardImage;
import com.example.pro.board.dto.*;
import com.example.pro.board.exception.BoardErrorCode;
import com.example.pro.board.exception.BoardException;
import com.example.pro.board.exception.BoardUnauthorizedException;
import com.example.pro.board.service.BoardImageService;
import com.example.pro.board.service.BoardService;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.docs.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest extends ControllerTest {
    @Mock
    private BoardService boardService;
    @Mock
    private BoardImageService boardImageService;
    @Mock
    private AuthService authService;

    public static final String URL = "https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg";

    static MultipartFile file = new MockMultipartFile("ForTest", new byte[]{});
    static Long boardId = 1L;

    static Board board;
    static Member member;

    @BeforeEach
    void setUp() throws Exception {

        member = Member.builder()
                .username("ajeong7038")
                .password("password1234")
                .nickname("ajeong")
                .email("ajung7038@gmail.com")
                .build();

        board = Board.builder()
                .username(member.getUsername())
                .title("제목")
                .content("내용")
                .image(null)
                .build();
    }

    @Test
    @DisplayName("[성공] 게시물 생성")
    void create() throws Exception{

        List<MultipartFile> fileList = new ArrayList<>();
        MockMultipartFile image = new MockMultipartFile("image", "imageFile.jpeg", MediaType.IMAGE_JPEG_VALUE, "<<jpeg data>>".getBytes());
        fileList.add(image);

        BoardSaveDto dto = BoardSaveDto.builder()
                .title("제목")
                .content("내용")
                .images(fileList)
                .build();

        board = Board.builder()
                .username(member.getUsername())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();


        when(authService.loadUser()).thenReturn(member);
        when(boardService.createBoard(any(), any())).thenReturn(board);

        MockMultipartHttpServletRequestBuilder builder = multipart("/boards");

        builder.with(request -> {
            request.setMethod("POST");
            return request;
        });

        ResultActions perform = mockMvc.perform(builder
                .file(image)
                .param("title", "제목")
                .param("content", "내용")
                .contentType(MediaType.APPLICATION_JSON));

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response", containsString("게시물 생성에 성공하였습니다. 게시물id:")));

        perform.andDo(document("board creation-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board") // 큰 태그
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response").type(JsonFieldType.STRING).description("응답 메시지")
                        ).build())));
    }
    
    @Test
    @DisplayName("[실패] 게시물 생성 - 유효성 검사 실패")
    public void createWithNullTitleAndContent() throws Exception {
        // given
        BoardSaveDto dto = BoardSaveDto.builder()
                .title(null)
                .content(null)
                .build();

        String body = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                        .locale(Locale.KOREA)
                .content(body));

        // ControllerTest
        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BAD_INPUT"))
                .andExpect(jsonPath("$.response.errorMessage").value("입력이 올바르지 않습니다."))
                .andExpect(jsonPath("$.response.errors.title").value("공백일 수 없습니다"))
                .andExpect(jsonPath("$.response.errors.content").value("공백일 수 없습니다"));

        // 문서 자동화
        perform.andDo(document("board creation-validation failed",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors.title").type(JsonFieldType.STRING).description("게시글 제목 공백 검사"),
                                fieldWithPath("response.errors.content").type(JsonFieldType.STRING).description("게시글 내용 공백 검사")
                        ).build())
                ));
    }

    @Test
    @DisplayName("[성공] 전체 게시물 조회")
    void findAll() throws Exception {
        List<BoardListResponseDto> boards = new ArrayList<>();
        BoardListResponseDto dto = BoardListResponseDto.builder()
                .id(1L)
                .username("ajeong7038")
                .title("제목")
                .createdAt("2024-02-07 18:32:25")
                .content("내용")
                .build();
        boards.add(dto);
        BoardCountResponseDto response = new BoardCountResponseDto(boards, 1L);

        when(boardService.findAllBoards()).thenReturn(response);

        ResultActions perform = mockMvc.perform(get("/boards")
                .contentType(MediaType.APPLICATION_JSON)
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.total").value(boards.size()))
                .andExpect(jsonPath("$.response.boards[0].id").value(dto.getId()))
                .andExpect(jsonPath("$.response.boards[0].username").value(dto.getUsername()))
                .andExpect(jsonPath("$.response.boards[0].isWriterQuit").value(false))
                .andExpect(jsonPath("$.response.boards[0].title").value(dto.getTitle()))
                .andExpect(jsonPath("$.response.boards[0].createdAt").value(dto.getCreatedAt()))
                .andExpect(jsonPath("$.response.boards[0].content").value(dto.getContent()));

        // 문서 자동화
        perform.andDo(document("board findAll-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.total").type(JsonFieldType.NUMBER).description("응답 메시지 - 댓글 총 개수"),
                                fieldWithPath("response.boards[].id").type(JsonFieldType.NUMBER).description("응답 메시지 - 아이디"),
                                fieldWithPath("response.boards[].username").type(JsonFieldType.STRING).description("응답 메시지 - 유저명"),
                                fieldWithPath("response.boards[].isWriterQuit").type(JsonFieldType.BOOLEAN).description("응답 메시지 - 작성자 탈퇴 여부"),
                                fieldWithPath("response.boards[].title").type(JsonFieldType.STRING).description("응답 메시지 - 제목"),
                                fieldWithPath("response.boards[].createdAt").type(JsonFieldType.STRING).description("응답 메시지 - 생성 날짜"),
                                fieldWithPath("response.boards[].content").type(JsonFieldType.STRING).description("응답 메시지 - 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[성공] 게시물 조회")
    void findById() throws Exception{
        List<String> urlList = new ArrayList<>();
        urlList.add("https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg");

        BoardImage boardImage = BoardImage.builder()
                .board(board)
                .url(urlList.get(0))
                .build();

        List<BoardImage> boardImages = new ArrayList<>();
        boardImages.add(boardImage);

        board = Board.builder()
                .username(member.getUsername())
                .title("제목")
                .content("내용")
                .image(boardImages)
                .build();

        Comment comment = Comment.builder()
                .username("comment-writer")
                .id(1L)
                .board(board)
                .content("댓글1 빈칸 아님")
                .build();

        Reply reply = Reply.builder()
                .id(1L)
                .username("reply-writer")
                .content("답글 내용 빈칸 아님")
                .comment(comment)
                .build();

        board.getComments().add(comment);
        comment.getReplies().add(reply);

        BoardResponseDto dto = BoardResponseDto.toBoardResponse(board, urlList);

        when(boardService.makeBoardResponse(any())).thenReturn(dto);

        ResultActions perform = mockMvc.perform(get("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.username").value(dto.getUsername()))
                .andExpect(jsonPath("$.response.isWriterQuit").value(false))
                .andExpect(jsonPath("$.response.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.response.content").value(dto.getContent()))
                .andExpect(jsonPath("$.response.createdAt").value(dto.getCreatedAt()))
                .andExpect(jsonPath("$.response.urlList").value(dto.getUrlList()))
                .andExpect(jsonPath("$.response.comments[0].commentId").value(comment.getId()))
                .andExpect(jsonPath("$.response.comments[0].username").value(comment.getWriter().getUsername()))
                .andExpect(jsonPath("$.response.comments[0].isWriterQuit").value(comment.getWriter().isMemberQuit()))
                .andExpect(jsonPath("$.response.comments[0].content").value(comment.getContent()))
                .andExpect(jsonPath("$.response.comments[0].createdAt").value(comment.getCreatedAt()))
                .andExpect(jsonPath("$.response.comments[0].isDeleted").value(false))
                .andExpect(jsonPath("$.response.comments[0].replies[0].replyId").value(reply.getId()))
                .andExpect(jsonPath("$.response.comments[0].replies[0].username").value(reply.getWriter().getUsername()))
                .andExpect(jsonPath("$.response.comments[0].replies[0].isWriterQuit").value(reply.getWriter().isMemberQuit()))
                .andExpect(jsonPath("$.response.comments[0].replies[0].content").value(reply.getContent()))
                .andExpect(jsonPath("$.response.comments[0].replies[0].createdAt").value(reply.getCreatedAt()))
                .andExpect(jsonPath("$.response.comments[0].replies[0].isDeleted").value(false));

        // 문서 자동화
        perform.andDo(document("board findById-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.username").type(JsonFieldType.STRING).description("응답 메시지 - 유저 아이디"),
                                fieldWithPath("response.isWriterQuit").type(JsonFieldType.BOOLEAN).description("응답 메시지 - 댓글 작성자 탈퇴 여부"),
                                fieldWithPath("response.title").type(JsonFieldType.STRING).description("응답 메시지 - 제목"),
                                fieldWithPath("response.content").type(JsonFieldType.STRING).description("응답 메시지 - 내용"),
                                fieldWithPath("response.urlList").type(JsonFieldType.ARRAY).description("응답 메시지 - url"),
                                fieldWithPath("response.createdAt").type(JsonFieldType.NULL).description("응답 메시지 - 생성 날짜"),
                                fieldWithPath("response.comments[].commentId").type(JsonFieldType.NUMBER).description("응답 메시지 - 댓글 아이디"),
                                fieldWithPath("response.comments[].username").type(JsonFieldType.STRING).description("응답 메시지 - 댓글 작성자 아이디"),
                                fieldWithPath("response.comments[].isWriterQuit").type(JsonFieldType.BOOLEAN).description("응답 메시지 - 댓글 작성자 탈퇴 여부"),
                                fieldWithPath("response.comments[].content").type(JsonFieldType.STRING).description("응답 메시지 - 댓글 내용"),
                                fieldWithPath("response.comments[].createdAt").type(JsonFieldType.NULL).description("응답 메시지 - 댓글 작성일자"),
                                fieldWithPath("response.comments[].isDeleted").type(JsonFieldType.BOOLEAN).description("응답 메시지 - 댓글 삭제 여부"),
                                fieldWithPath("response.comments[].replies[].replyId").type(JsonFieldType.NUMBER).description("응답 메시지 - 답글 아이디"),
                                fieldWithPath("response.comments[].replies[].username").type(JsonFieldType.STRING).description("응답 메시지 - 답글 작성자 아이디"),
                                fieldWithPath("response.comments[].replies[].isWriterQuit").type(JsonFieldType.BOOLEAN).description("응답 메시지 - 댓글 작성자 탈퇴 여부"),
                                fieldWithPath("response.comments[].replies[].content").type(JsonFieldType.STRING).description("응답 메시지 - 답글 내용"),
                                fieldWithPath("response.comments[].replies[].createdAt").type(JsonFieldType.NULL).description("응답 메시지 - 답글 작성일자"),
                                fieldWithPath("response.comments[].replies[].isDeleted").type(JsonFieldType.BOOLEAN).description("응답 메시지 - 답글 삭제 여부")

                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 게시물 조회 - 게시물을 찾을 수 없는 경우")
    void findByIdWithBoardNull() throws Exception{
        when(boardService.makeBoardResponse(any())).thenThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        ResultActions perform = mockMvc.perform(get("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BoardException.class))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BOARD_NOT_FOUND"))
                .andExpect(jsonPath("$.response.errorMessage").value("게시물을 찾을 수 없습니다."));

        // 문서 자동화
        perform.andDo(document("board findById-board not found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[성공] 게시물 수정")
    void update() throws Exception{
        BoardUpdateDto dto = BoardUpdateDto.builder()
                .title("제목(new)")
                .content("내용(new)")
                .build();

        when(boardService.updateBoard(any(), any(), any())).thenReturn(dto);
        String body = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(put("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.title").value("제목(new)"))
                .andExpect(jsonPath("$.response.content").value("내용(new)"));

        // 문서 자동화
        perform.andDo(document("board update-success",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.title").type(JsonFieldType.STRING).description("제목 업데이트 여부 검사"),
                                fieldWithPath("response.content").type(JsonFieldType.STRING).description("내용 업데이트 여부 검사")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 게시물 수정 - 유효성 검사 실패")
    void updateWithTitleAndContentNull() throws Exception{
        BoardUpdateDto dto = BoardUpdateDto.builder()
                .title(null)
                .content("  ")
                .build();

        String body = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(put("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.KOREA)
                .content(body)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BAD_INPUT"))
                .andExpect(jsonPath("$.response.errorMessage").value("입력이 올바르지 않습니다."))
                .andExpect(jsonPath("$.response.errors.title").value("공백일 수 없습니다"))
                .andExpect(jsonPath("$.response.errors.content").value("공백일 수 없습니다"));

        // 문서 자동화
        perform.andDo(document("board update-validation failed",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors.title").type(JsonFieldType.STRING).description("제목 공백 검사"),
                                fieldWithPath("response.errors.content").type(JsonFieldType.STRING).description("내용 공백 검사")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 게시물 수정 - 게시물을 찾을 수 없는 경우")
    void updateWithBoardNull() throws Exception{

        BoardUpdateDto dto = BoardUpdateDto.builder()
                .title("null")
                .content("null")
                .build();

        when(boardService.updateBoard(any(), any(), any())).thenThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        String body = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(put("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BoardException.class))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BOARD_NOT_FOUND"))
                .andExpect(jsonPath("$.response.errorMessage").value("게시물을 찾을 수 없습니다."));

        // 문서 자동화
        perform.andDo(document("board create-board not found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 게시물 수정 - 권한이 없는 경우")
    void updateBoardNotValidation() throws Exception {
        BoardUpdateDto dto = BoardUpdateDto.builder()
                .title("제목")
                .content("내용")
                .build();

        when(boardService.updateBoard(any(), any(), any())).thenThrow(new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_BOARD));
        String body = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(put("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        perform.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BoardUnauthorizedException.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("UNAUTHORIZED_BOARD"))
                .andExpect(jsonPath("$.response.errorMessage").value("게시물 권한이 없습니다."));

        // 문서 자동화
        perform.andDo(document("board update-unAuthorized board",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[성공] 게시물 삭제")
    void deleteBoard() throws Exception{

        ResultActions perform = mockMvc.perform(delete("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON));

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value("게시물 삭제에 성공하였습니다. 게시물id: " + boardId));

        perform.andDo(document("board delete-success"
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
    @DisplayName("[실패] 게시물 삭제 - 게시물을 찾을 수 없는 경우")
    void deleteWithBoardNull() throws Exception{
        doThrow(new BoardException(BoardErrorCode.BOARD_NOT_FOUND))
                .when(boardService)
                .deleteBoard(any(), any());
        ResultActions perform = mockMvc.perform(delete("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BoardException.class))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("BOARD_NOT_FOUND"))
                .andExpect(jsonPath("$.response.errorMessage").value("게시물을 찾을 수 없습니다."));

        // 문서 자동화
        perform.andDo(document("board delete-board not found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 정상 여부"),
                                fieldWithPath("response.errorCode").type(JsonFieldType.STRING).description("예외 코드"),
                                fieldWithPath("response.errorMessage").type(JsonFieldType.STRING).description("예외 메시지"),
                                fieldWithPath("response.errors").type(JsonFieldType.OBJECT).description("필드 유효성 검사 내용")
                        ).build())
        ));
    }

    @Test
    @DisplayName("[실패] 게시물 삭제 - 권한이 없는 경우")
    void deleteBoardNotValidation() throws Exception {
        doThrow(new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_BOARD))
                .when(boardService)
                .deleteBoard(any(), any());
        ResultActions perform = mockMvc.perform(delete("/boards/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        perform.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(BoardUnauthorizedException.class))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response.errorCode").value("UNAUTHORIZED_BOARD"))
                .andExpect(jsonPath("$.response.errorMessage").value("게시물 권한이 없습니다."));
        // 문서 자동화
        perform.andDo(document("board delete- unauthorized board",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                        .tag("API-Board")
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
        return new BoardController(boardService, boardImageService, authService);
    }
}