package com.example.pro.auth.domain;

import com.example.pro.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.example.pro.common.exception.Validator.validEmail;
import static com.example.pro.common.exception.Validator.validString;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String nickname;
    private String profile;
    @NotNull
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String username, String password, String nickname, String email) {
        this.username = validString(username);
        this.password = validString(password);
        this.nickname = validString(nickname);
        this.email = validEmail(email);
        this.role = Role.USER;
    }

    public Member updateProfile(String profileUrl) {
        this.profile = profileUrl;
        return this;
    }
}
