package com.example.pro.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    // user
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> image = new ArrayList<>();

    @Column(length = 50, nullable = false)
    private String title;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment = new ArrayList<>();

    private String content;
    private LocalDateTime created_at; // Date

    /**
     * 생성 메서드
     */
    // 유저 추가
    @Builder
//    public Board (Long userId, String title, String content) {
    public Board (String title, String content) {
        // 사진?
//        this.user = user.getUserId(); ??
        this.title = title;
        this.content = content;
        this.created_at = LocalDateTime.now();
    }
}
