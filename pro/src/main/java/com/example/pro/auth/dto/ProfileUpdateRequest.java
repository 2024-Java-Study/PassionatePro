package com.example.pro.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileUpdateRequest {

    @NotNull
    MultipartFile image;

    public ProfileUpdateRequest(MultipartFile image) {
        this.image = image;
    }
}
