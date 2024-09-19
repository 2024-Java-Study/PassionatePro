package com.example.pro.board.dto;

import com.example.pro.board.domain.Board;

public record BoardWithWriterDto(Board board, String userProfile) {
}
