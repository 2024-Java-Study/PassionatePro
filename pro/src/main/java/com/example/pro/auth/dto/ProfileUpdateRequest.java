package com.example.pro.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileUpdateRequest {
    MultipartFile image;

    public ProfileUpdateRequest(MultipartFile image) {
        this.image = image;
    }
}
