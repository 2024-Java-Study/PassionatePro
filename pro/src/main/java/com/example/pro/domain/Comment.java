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
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment")
    private List<Reply> reply = new ArrayList<>();

    private String content;
    private LocalDateTime created_at;

    /**
     * 생성 메서드
     */
    @Builder
//    public Comment(User user, Board board, String content) {
    public Comment(Board board, String content) {
//        this.user = user;
        this.board = board;
        this.content = content;
        this.created_at = LocalDateTime.now();
    }
}
