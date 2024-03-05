package com.example.pro.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record ReplyUpdateRequestDto(@NotBlank String content) {
}
