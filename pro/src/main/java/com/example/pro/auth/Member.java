package com.example.pro.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.pro.common.exception.Validator.validEmail;
import static com.example.pro.common.exception.Validator.validString;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;

    @Builder
    public Member(String username, String password, String email) {
        this.username = validString(username);
        this.password = validString(password);
        this.email = validEmail(email);
    }
}
