package com.example.pro.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequestDto(@NotBlank String content) {
}
