package com.example.pro.board;

import com.example.pro.domain.BoardImage;
import com.example.pro.domain.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    // Member
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Member member;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> image = new ArrayList<>();

    @Column(length = 50, nullable = false)
    private String title;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime created_at; // Date

    /**
     * 생성 메서드
     */
    // 유저 추가
    @Builder
//    public Board (Member member, String title, String content) {
    public Board (String title, String content) {
        // 사진?
//        this.user = user.getUserId(); ??
        this.title = title;
        this.content = content;
//        this.created_at = LocalDateTime.now();
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
