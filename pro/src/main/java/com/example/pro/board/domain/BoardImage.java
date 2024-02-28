package com.example.pro.board.domain;

import com.example.pro.board.domain.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(unique = true)
    private String url;

    /**
     * 생성 메서드
     */
    @Builder
    public BoardImage (Board board, String url) {
        this.board = board;
        this.url = url;
    }
}
