package com.example.pro.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    Board findByUserId(Long userId);
}