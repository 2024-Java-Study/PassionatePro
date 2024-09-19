package com.example.pro.auth.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.dto.MyInfoRequest;
import com.example.pro.board.domain.Board;
import com.example.pro.board.repository.BoardRepository;
import com.example.pro.comment.domain.Comment;
import com.example.pro.comment.domain.Reply;
import com.example.pro.comment.repository.CommentRepository;
import com.example.pro.comment.repository.ReplyRepository;
import com.example.pro.files.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final FileUploader fileUploader;
    static final String PROFILE_KEY = "profiles/";

    @Transactional
    public Member updateProfile(MultipartFile file, Member member) {
        if (file.isEmpty()) {
            fileUploader.deleteFile(member.getProfile(), PROFILE_KEY);
            return member.deleteProfile();
        }

        String url = fileUploader.uploadFile(file, PROFILE_KEY);
        return member.updateProfile(url);
    }

    public void markQuitInWriterInfo(Member member) {
        List<Board> boards = boardRepository.findAllByWriter(member.getUsername());
        List<Comment> comments = commentRepository.findAllByWriter(member.getUsername());
        List<Reply> replies = replyRepository.findAllByWriter(member.getUsername());

        boards.forEach(Board::removeWriterInfo);
        comments.forEach(Comment::removeWriterInfo);
        replies.forEach(Reply::removeWriterInfo);
    }

    public MyInfoRequest myPageInfo(Member member) {
        return MyInfoRequest.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .email(member.getEmail())
                .build();
    }
}
