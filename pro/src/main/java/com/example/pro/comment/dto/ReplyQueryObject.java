package com.example.pro.comment.dto;

import com.example.pro.comment.domain.Reply;

public record ReplyQueryObject(Reply reply, String writerProfile) {
}
