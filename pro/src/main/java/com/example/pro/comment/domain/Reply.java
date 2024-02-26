package com.example.pro.comment.domain;

import com.example.pro.auth.domain.Member;
import com.example.pro.common.BaseTimeEntity;
import com.example.pro.common.exception.Validator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @NotBlank
    private String content;

    /**
     * 생성 메서드
     */
    public Reply(Member member, Comment comment, String content) {
        this.member = member;
        this.comment = comment;
        this.content = Validator.validString(content);
    }
}