package com.example.pro.auth.dto;

import com.example.pro.auth.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {
    private String username;
    // 사진?

    @Builder
    public BoardRequest(String username) {
        this.username = username;
    }

    public static BoardRequest toMemberDto(Member member) {
        return BoardRequest.builder()
                .username(member.getUsername())
                .build();
    }
}
