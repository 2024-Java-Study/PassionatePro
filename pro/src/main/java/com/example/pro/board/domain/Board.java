package com.example.pro.board.domain;

import com.example.pro.comment.domain.WriterInfo;
import com.example.pro.common.BaseTimeEntity;
import com.example.pro.comment.domain.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private WriterInfo writerInfo;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardImage> image = new ArrayList<>();

    @Column(length = 50, nullable = false)
    @NotBlank
    private String title;

    @OneToMany(mappedBy = "board")
    private final List<Comment> comments = new ArrayList<>();
    
    @Column(nullable = false)
    @NotBlank
    private String content;

    /**
     * 생성 메서드
     */
    // 유저 추가
    @Builder
    public Board (Long id, String username, String profile, String title, String content, List<BoardImage> image) {
        this.id = id;
        this.writerInfo = new WriterInfo(username, profile,false);
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void removeWriterInfo() {
        this.writerInfo = new WriterInfo("탈퇴한 사용자", null, true);
    }

    public void uploadFile(List<BoardImage> images) {
        this.image = images;
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}
