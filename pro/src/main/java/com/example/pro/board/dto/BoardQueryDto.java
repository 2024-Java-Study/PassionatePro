package com.example.pro.board.dto;

import com.example.pro.board.domain.BoardImage;
import com.example.pro.comment.dto.CommentQueryObject;
import com.example.pro.comment.dto.ReplyQueryObject;

import java.util.List;
import java.util.Map;

public record BoardQueryDto(BoardWithWriterDto board, List<BoardImage> images, List<CommentQueryObject> comments, Map<Long, List<ReplyQueryObject>> repliesMap) {
}
