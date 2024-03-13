package com.example.pro.comment.domain;

import com.example.pro.common.BaseTimeEntity;
import com.example.pro.common.BooleanTypeConverter;
import com.example.pro.common.exception.Validator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.LongStream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Embedded
    private WriterInfo writer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @NotBlank
    private String content;

    @NotNull
    @Convert(converter = BooleanTypeConverter.class)
    private boolean isDeleted;

    /**
     * 생성 메서드
     */
    @Builder
    public Reply(Long id, String username, Comment comment, String content) {
        this.id = id;
        this.writer = new WriterInfo(username, false);
        this.comment = comment;
        this.content = Validator.validString(content);
        this.isDeleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteReply() {
        this.isDeleted = true;
    }

    public boolean isTheYoungest() {
        List<Reply> replies = this.comment.getReplies();
        long idx = replies.indexOf(this);
        return LongStream.range(idx + 1, replies.size()).allMatch(i -> replies.get((int) i).isDeleted);
    }
}
