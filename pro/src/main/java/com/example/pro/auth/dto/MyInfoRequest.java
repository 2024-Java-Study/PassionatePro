package com.example.pro.auth.dto;

import lombok.Builder;

@Builder
public record MyInfoRequest(String username, String nickname, String profile, String email) {
}
