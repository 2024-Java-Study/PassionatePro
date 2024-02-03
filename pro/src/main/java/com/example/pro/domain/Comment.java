package com.example.pro.domain;

import com.example.pro.board.domain.Board;
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
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "comment")
    private List<Reply> reply = new ArrayList<>();

    @Column(nullable = false)
    private String content;
    private LocalDateTime created_at;

    /**
     * 생성 메서드
     */
    @Builder
//    public Comment(Member member, Board board, String content) {
    public Comment(Board board, String content) {
//        this.member = member;
        this.board = board;
        this.content = content;
        this.created_at = LocalDateTime.now();
    }
}
