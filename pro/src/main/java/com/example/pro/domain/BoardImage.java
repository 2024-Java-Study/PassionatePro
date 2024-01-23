package com.example.pro.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_image")
@Getter
@NoArgsConstructor
public class BoardImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(unique = true)
    private String board_image_url;

    /**
     * 생성 메서드
     */
    @Builder
    public BoardImage (Board board, String boardImageUrl) {
        this.board = board;
        this.board_image_url = boardImageUrl;
    }
}
