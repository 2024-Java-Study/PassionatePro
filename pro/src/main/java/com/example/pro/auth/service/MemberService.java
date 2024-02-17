package com.example.pro.auth.service;

import com.example.pro.auth.domain.Member;
import com.example.pro.files.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final AuthService authService;
    private final FileUploader fileUploader;
    static final String PROFILE_KEY = "profiles/";

    @Transactional
    public Member updateProfile(MultipartFile file, Member member) {
        String url = fileUploader.uploadFile(file, PROFILE_KEY);
        return member.updateProfile(url);
    }
}
