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
    private String profile;

    @Builder
    public BoardRequest(String username, String profile) {
        this.username = username;
        this.profile = profile;
    }

    public static BoardRequest toMemberDto(Member member) {
        return BoardRequest.builder()
                .username(member.getUsername())
                .profile(member.getProfile())
                .build();
    }
}
