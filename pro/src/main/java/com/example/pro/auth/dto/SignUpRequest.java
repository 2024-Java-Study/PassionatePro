package com.example.pro.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;

    public static Member toMember(SignUpRequest dto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(dto.username)
                .password(passwordEncoder.encode(dto.password))
                .email(dto.email)
                .build();
    }
}
