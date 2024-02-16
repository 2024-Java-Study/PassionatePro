package com.example.pro.board.domain;

import com.example.pro.auth.domain.Member;
import com.example.pro.auth.dto.BoardRequest;
import com.example.pro.common.BaseTimeEntity;
import com.example.pro.domain.BoardImage;
import com.example.pro.comment.domain.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    // Member
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> image = new ArrayList<>();

    @Column(length = 50, nullable = false)
    @NotBlank
    private String title;

    @OneToMany(mappedBy = "board")
    private List<Comment> comment = new ArrayList<>();

    @Column(nullable = false)
    @NotBlank
    private String content;

    /**
     * 생성 메서드
     */
    // 유저 추가
    @Builder
    public Board (Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
