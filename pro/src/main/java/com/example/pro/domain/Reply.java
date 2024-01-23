package com.example.pro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;
    private LocalDateTime created_at;

    /**
     * 생성 메서드
     */
//    public Reply(Member member, Board board, String content) {
    public Reply(Comment comment, String content) {
//        this.member = member;
        // board?
        this.comment = comment;
        this.content = content;
        this.created_at = LocalDateTime.now();
    }
}
