package com.example.pro.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {

    private Long id;
    private String title;
    private String content;

    @Builder
//    public BoardDto (Member member, String title, String content) {
    public BoardDto (String title, String content) {
//        this.member = user.getId();
        this.title = title;
        this.content = content;
    }
}
