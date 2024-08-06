package com.example.pro.auth.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.board.domain.Board;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.repository.CommentRepository;
import com.example.pro.comment.repository.ReplyRepository;
import com.example.pro.files.FileUploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private Member member;
    private MultipartFile file;
    @InjectMocks
    private MemberService memberService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private FileUploader fileUploader;

    @BeforeEach
    void init() {
        member = Member.builder()
                .username("username")
                .password("password1234")
                .nickname("nickname")
                .email("helloworld@gmail.com")
                .build();

        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/free-icon-banana.png")) {
            file = new MockMultipartFile("ForTest", fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException("File Not Found");
        }
    }

    @Test
    @DisplayName("[성공] 프로필 이미지 수정")
    void updateProfile() {
        Assertions.assertNull(member.getProfile());
        when(fileUploader.uploadFile(any(), any()))
                .thenReturn("https://passionate-pro-bucket.s3.ap-northeast-2.amazonaws.com/test/ForTest.jpeg");
        Member updatedMember = memberService.updateProfile(file, member);
        assertThat(updatedMember.getProfile()).isNotBlank();
    }

    @Test
    @DisplayName("[성공] 작성글 익명 처리")
    void markQuitWriterInfo() {
        Board board = Board.builder()
                .id(1L)
                .username(member.getUsername())
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .board(board)
                .username(member.getUsername())
                .content("댓글 내용 빈칸 아님")
                .build();

        Reply reply = Reply.builder()
                .id(1L)
                .username(member.getUsername())
                .comment(comment)
                .content("대댓글 내용")
                .build();

        board.getComments().add(comment);
        comment.getReplies().add(reply);

        List<Board> boards = List.of(board);
        List<Comment> comments = List.of(comment);
        List<Reply> replies = List.of(reply);
        when(boardRepository.findAllByWriter(any())).thenReturn(boards);
        when(commentRepository.findAllByWriter(any())).thenReturn(comments);
        when(replyRepository.findAllByWriter(any())).thenReturn(replies);

        memberService.markQuitInWriterInfo(member);
        assertThat(board.getWriterInfo().isMemberQuit()).isTrue();
        assertThat(comment.getWriter().isMemberQuit()).isTrue();
        assertThat(reply.getWriter().isMemberQuit()).isTrue();
    }
}