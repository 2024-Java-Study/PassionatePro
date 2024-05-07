package com.example.pro.comment.domain;

import com.example.pro.board.domain.Board;
import com.example.pro.common.BaseTimeEntity;
import com.example.pro.common.BooleanTypeConverter;
import com.example.pro.common.exception.Validator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Embedded
    private WriterInfo writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "comment")
    private final List<Reply> replies = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @NotNull
    @Convert(converter = BooleanTypeConverter.class)
    private boolean isDeleted;

    /**
     * 생성 메서드
     */
    @Builder
    public Comment(Long id, String username, Board board, String content) {
        this.id = id;
        this.writer = new WriterInfo(username, false);
        this.board = board;
        this.content = Validator.validString(content);
        this.isDeleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void removeWriterInfo() {
        this.writer = new WriterInfo("탈퇴한 사용자", true);
    }

    public void deleteComment() {
        this.isDeleted = true;
    }

    public boolean hasNoReplies() {
        return this.replies.size() == 0;
    }

    public Long countExistingReplies() {
        return this.replies.stream().filter(reply -> !reply.isDeleted()).count();
    }
}
