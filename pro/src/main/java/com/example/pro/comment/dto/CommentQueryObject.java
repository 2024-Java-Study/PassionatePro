package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Comment;

public record CommentQueryObject(Comment comment, String writerProfile) {
}
