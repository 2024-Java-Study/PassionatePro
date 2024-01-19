package com.example.pro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
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
    private Date created_at; // LocalDateTime
}
